package com.simple.ged.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.simple.ged.dao.GedDirectoryRepository;
import com.simple.ged.models.GedDirectory;
import com.simple.ged.tools.SpringFactory;

/**
 * 
 * Provide method for directory manipulation
 * 
 * See also GedDocumentService for delete and rename methods
 * 
 * @author xavier
 * 
 */
@Service
public final class GedDirectoryService {

	private GedDirectoryRepository directoryRepository;
	
	
	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(GedDirectoryService.class);
	
	
	private GedDirectoryService() {
		directoryRepository = SpringFactory.getAppContext().getBean(GedDirectoryRepository.class);
	}
	
	/**
	 * 
	 * @param directoryPath
	 *            The directory path, relative to ged root
	 */
	public GedDirectory findDirectoryByDirectoryPath(String directoryPath) {
		logger.debug("Find directory for path : {}", directoryPath);
		return directoryRepository.findByDirectoryPath(GedDocumentService.forceUnixSeparator(directoryPath));
	}

	
	/**
	 * Add or update the given document
	 */
	public void save(GedDirectory directory) {
		if (directory.getRelativeDirectoryPath().startsWith("/")) {
			directory.setRelativeDirectoryPath(directory.getRelativeDirectoryPath().replaceFirst("/", ""));
		}
		directoryRepository.saveAndFlush(directory);
	}
	
	
	/**
	 * Rename a directory (same treatment)
	 * 
	 * @param oldName
	 *            The old name contains the relative path to file
	 * 
	 * @param newName
	 *            The new name contains the relative path to file, with the new
	 *            name =)
	 */
	public void updateDirectoryPath(String oldName, String newName) {
		logger.debug("Rename : " + oldName + " to " + newName);
		List<GedDirectory> directoriesToUpdate = directoryRepository.findByDirectoryPathStartingWith(oldName);

		for (GedDirectory directory : directoriesToUpdate) {
			directory.setRelativeDirectoryPath(directory.getRelativeDirectoryPath().replaceFirst(oldName, newName));
			directoryRepository.saveAndFlush(directory);
		}
	}

	
	/**
	 * Delete a document file
	 * 
	 * If some document as not at least on attached file after delete, it's removed
	 */
	public void deleteDirectory(String directoryPath) {
		logger.debug("Remove directory : " + directoryPath);
		
		List<GedDirectory> directoriesToDelete = directoryRepository.findByDirectoryPathStartingWith(directoryPath);
		for (GedDirectory directory : directoriesToDelete) {
			directoryRepository.delete(directory);
		}
	}
	
}
