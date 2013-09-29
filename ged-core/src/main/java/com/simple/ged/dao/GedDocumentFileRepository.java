package com.simple.ged.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simple.ged.models.GedDocumentFile;


/**
 * 
 * Repository for document files.
 * 
 * @author Xavier
 *
 */
public interface GedDocumentFileRepository extends JpaRepository<GedDocumentFile, Integer> {

	/**
	 * @param filePath
	 *            The file path, relative to ged root
	 */
	public GedDocumentFile findByRelativeFilePath(String relativefilePath);
	

	
	public List<GedDocumentFile> findByRelativeFilePathStartingWith(String relativefilePath);
	
}
