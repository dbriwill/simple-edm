package com.simple.ged.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import com.simple.ged.Profile;
import com.simple.ged.dao.DocumentDAO;
import com.simple.ged.models.GedDocument;
import com.simple.ged.models.GedDocumentFile;
import com.simple.ged.models.GedDocumentPhysicalLocation;
import com.simple.ged.tools.FileHelper;

import fr.xmichel.javafx.dialog.Dialog;

/**
 * @class GedDocumentService
 * 
 * Provide method for document manipulation
 * 
 * @author xavier
 *
 */
public class GedDocumentService {

	private static final Logger logger = Logger.getLogger(GedDocumentService.class);

	
	/**
	 * Replace \\ by /, to keep unix like path in database
	 */
	private static String forceUnixSeparator(String s) {
		return s.replaceAll(Matcher.quoteReplacement("\\"), Matcher.quoteReplacement("/"));
	}
	
	
	/**
	 * 
	 * @param filePath
	 *            The file path, relative to ged root
	 */
	public static GedDocument findDocumentbyFilePath(String filePath) {
		return DocumentDAO.findDocumentbyFilePath(forceUnixSeparator(filePath));
	}
	
	
	/**
	 * 
	 * @param filePath
	 *            The file path, relative to ged root
	 */
	public static GedDocument getDocumentFromFile(String filePath)
	{
		return DocumentDAO.findDocumentbyFilePath(forceUnixSeparator(filePath));
	}
	
	/**
	 * Add or update the given document
	 */
	public static void addOrUpdateDocument(GedDocument doc)
	{
		DocumentDAO.saveOrUpdate(doc);
	}
	
	/**
	 * Rename some ged document file (give relative file path)
	 * 
	 * @param oldName
	 * 				The old file name
	 * @param newName
	 * 				The new file name
	 */
	public static void renameDocumentFile(String oldName, String newName)
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
			logger.fatal("Impossible de déplacer le fichier", e);
			Dialog.showThrowable("Impossible de renommer/déplacer le fichier", "Le renommage/déplacement du fichier a échoué :", e);
		}
		
		// rename in database
		DocumentDAO.updateFilePath(forceUnixSeparator(oldName), forceUnixSeparator(newName));
	}
	
	
	/**
	 * Delete some document
	 */
	public static void deleteDocumentFile(String filePath)
	{
		try {
			FileHelper.recursifDelete(new File(Profile.getInstance()
					.getLibraryRoot() + filePath));
		} catch (IOException e) {
			logger.error("Delete error");
		}
		
		DocumentDAO.deleteFile(forceUnixSeparator(filePath));
	}
	
	/**
	 * Get documents with the given location
	 */
	public static List<GedDocument> findDocumentbyLocation(GedDocumentPhysicalLocation location) {
		return DocumentDAO.findDocumentbyLocation(location);
	}
	
	/**
	 * Search for the given words
	 * 
	 * Words is a string where word are splited by space, and a matching item must match with any of the given words
	 */
	public static synchronized List<GedDocumentFile> searchForWords(String searchedWords) {
		String[] words = searchedWords.split(" ");
		
		// convert to java list
		List<String> wordList = new ArrayList<>();
		for (String w : words) {
			wordList.add(w);
		}
		
		return DocumentDAO.getDocumentWhichContainsEveryWords(wordList);
	}
	
}
