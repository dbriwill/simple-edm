package com.simple.ged.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.simple.ged.Profile;
import com.simple.ged.dao.GedDocumentRepository;
import com.simple.ged.models.GedDocument;
import com.simple.ged.models.GedDocumentFile;
import com.simple.ged.tools.FileHelper;
import com.simple.ged.tools.SpringFactory;

import fr.xmichel.javafx.dialog.Dialog;

/**
 * @class GedDocumentService
 * 
 * Provide method for document AND directory manipulation
 * 
 * @author xavier
 *
 */
@Service
public class GedDocumentService {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(GedDocumentService.class);

	
	private GedDocumentRepository gedDocumentRepository = SpringFactory.getAppContext().getBean(GedDocumentRepository.class);
	
	private GedDocumentFileService gedDocumentFileService = SpringFactory.getAppContext().getBean(GedDocumentFileService.class);
	
	private GedDirectoryService gedDirectoryService = SpringFactory.getAppContext().getBean(GedDirectoryService.class);
	
	
	
	/*** GET OUT THIS IN NEW CLASS ***/
	
	/**
	 * Replace \\ by /, to keep unix like path in database
	 */
	public static String forceUnixSeparator(String s) {
        // replace all \ by /
        s = s.replaceAll(Matcher.quoteReplacement("\\"), Matcher.quoteReplacement("/"));

        // replace all // by /
        while (s.contains(Matcher.quoteReplacement("//"))) {
            s = s.replaceAll("//", "/");
        }

        return s;
	}
	
	
	/**
	 * Get relative file path from the absolute path
	 */
	public String getRelativeFromAbsolutePath(String absolutePath) {
		return forceUnixSeparator(forceUnixSeparator(absolutePath).replaceFirst(forceUnixSeparator(Profile.getInstance().getLibraryRoot()), ""));
	}
	
	
	/************************************/
	
	
	
	/**
	 * 
	 * @param filePath
	 *            The file path, relative to ged root
	 */
	public GedDocument findDocumentByFilePath(String filePath) {
		return gedDocumentRepository.findByDocumentFilesRelativeFilePath(forceUnixSeparator(filePath));
	}

	
    /**
     * Ged document by id
     */
    public GedDocument findDocumentById(Integer id) {
        return gedDocumentRepository.findOne(id);
    }
	

	/**
	 * Add or update the given document
	 */
	public void save(GedDocument doc) {
		gedDocumentRepository.saveAndFlush(doc);
		ElasticSearchService.indexDocument(doc);
	}
	
	
	/**
	 * Rename some ged document file (give relative file path)
	 * 
	 * @param oldName
	 * 				The old file name
	 * @param newName
	 * 				The new file name
	 */
	public void renameDocumentFile(String oldName, String newName)
	{
		if (newName.isEmpty()) {
			return;
		}
		
		// physical renaming
		Path oldFilePath = Paths.get(Profile.getInstance().getLibraryRoot() + oldName);
		Path newFilePath = Paths.get(Profile.getInstance().getLibraryRoot() + newName);
		
		if (oldFilePath.equals(newFilePath)) {
			return;
		}
		
		try {
			logger.info("Move : (" + oldFilePath + " => " + newFilePath);
			Files.move(oldFilePath, newFilePath);
		} catch (IOException e) {
			logger.error("Move failed : (" + oldFilePath + " => " + newFilePath);
			logger.error("Impossible de déplacer le fichier", e);
			Dialog.showThrowable("Impossible de renommer/déplacer le fichier", "Le renommage/déplacement du fichier a échoué :", e);
			return;
		}
		
		String oldNameUnixStyle = forceUnixSeparator(oldName);
		String newNameUnixStyle = forceUnixSeparator(newName);
		
		if (oldNameUnixStyle.startsWith("/")) {
			oldNameUnixStyle = oldNameUnixStyle.replaceFirst("/", "");
		}
		if (newNameUnixStyle.startsWith("/")) {
			newNameUnixStyle = newNameUnixStyle.replaceFirst("/", "");
		}
		
		logger.debug("Old name : {}", oldNameUnixStyle);
		logger.debug("New name : {}", newNameUnixStyle);
		
		// rename in database
		gedDirectoryService.updateDirectoryPath(oldNameUnixStyle, newNameUnixStyle);
		

		List<GedDocumentFile> results = gedDocumentFileService.findByRelativeFilePathStartingWith(oldName);
		
		for (GedDocumentFile file : results) {
			file.setRelativeFilePath(file.getRelativeFilePath().replaceFirst(oldName, newName));
			gedDocumentFileService.save(file);
		}
	}
	
	
	/**
	 * Delete some document
	 */
	public void deleteDocumentFile(String filePath)
	{
		try {
			FileHelper.recursifDelete(new File(Profile.getInstance().getLibraryRoot() + filePath));
		} catch (IOException e) {
			logger.error("Delete error", e);
			Dialog.showThrowable("Impossible de supprimer le fichier", "La suppression du fichier a échoué :", e);
			return;
		}
		
		gedDirectoryService.deleteDirectory(forceUnixSeparator(filePath));
		
		List<GedDocumentFile> results = gedDocumentFileService.findByRelativeFilePathStartingWith(filePath);
		for (GedDocumentFile f : results) {
			f.getDocument().removeFile(f);
			gedDocumentRepository.save(f.getDocument());
			
			if (f.getDocument().getDocumentFiles().isEmpty()) {
				gedDocumentRepository.delete(f.getDocument());
			}
			
			gedDocumentFileService.delete(f);
		}
	}
	
	
	/**
	 * Search for the given words
	 * 
	 * Words is a string where word are splited by space, and a matching item must match with any of the given words
	 */
	public  List<GedDocumentFile> searchForWords(String searchedWords) {
        List<GedDocumentFile> results = new ArrayList<>();

        List<GedDocument> matchingDocuments = ElasticSearchService.basicSearch(searchedWords);
        logger.info("Matching document count : {}", matchingDocuments.size());
        
        for (GedDocument doc : matchingDocuments) {
            for (GedDocumentFile file : doc.getDocumentFiles()) {
                results.add(file);
            }
        }

        logger.info("Matching files count : {}", results.size());
        
		return results;
	}
	

    /**
     * Get all documents
	*/
    public List<GedDocument> findAll() {
        return gedDocumentRepository.findAll();
    }
	
}
