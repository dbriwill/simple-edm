package com.simple.ged.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.resthub.common.service.CrudServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import com.simple.ged.models.GedDirectory;
import com.simple.ged.services.GedDirectoryService;

import fr.xmichel.toolbox.hibernate.sqlite.HibernateUtil;

/**
 * Named DAO for directory icon customization
 * 
 * @author xavier
 * 
 */
public interface GedDirectoryRepository extends JpaRepository<GedDirectory, String> {

	/**
	 * @param directoryPath
	 *            The directory path, relative to ged root
	 */
	public GedDirectory findByDirectoryPath(String directoryPath);
	
	/**
	 * @param directoryPath
	 *            The directory path, relative to ged root
	 */
	public List<GedDirectory> findByDirectoryPathStartingWith(String directoryPath);

}
