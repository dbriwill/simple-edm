package com.simple.ged.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simple.ged.models.GedDirectory;

/**
 * Named DAO for directory icon customization
 * 
 * @author xavier
 * 
 */
public interface GedDirectoryRepository extends JpaRepository<GedDirectory, Integer> {

	/**
	 * @param relativeDirectoryPath
	 *            The directory path, relative to ged root
	 */
	public GedDirectory findByRelativeDirectoryPath(String relativeDirectoryPath);
	
	/**
	 * @param relativeDirectoryPath
	 *            The directory path, relative to ged root
	 */
	public List<GedDirectory> findByRelativeDirectoryPathStartingWith(String relativeDirectoryPath);

}
