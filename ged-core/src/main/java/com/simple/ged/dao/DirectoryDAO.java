package com.simple.ged.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.models.GedDirectory;

import fr.xmichel.toolbox.hibernate.sqlite.HibernateUtil;

/**
 * DAO for directory icon customization
 * 
 * @author xavier
 * 
 */
public final class DirectoryDAO {

	
	private static final Logger logger = LoggerFactory.getLogger(DirectoryDAO.class);
	
	/**
	 * Should not be instantiated
	 */
	private DirectoryDAO() {
	}
	
	/**
	 * 
	 * @param directoryPath
	 *            The directory path, relative to ged root
	 */
	public static synchronized GedDirectory findDirectorybyDirectoryPath(String directoryPath) {
		logger.debug("Get directory for file : " + directoryPath);

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(GedDirectory.class).add(Restrictions.eq("relativeDirectoryPath", directoryPath));  
		
		@SuppressWarnings("unchecked")
		List<GedDirectory> results = criteria.list();  

		GedDirectory d = null;

		if ( ! results.isEmpty() ) {
			d = results.get(0);
		}
		
		session.close();
		
		return d;
	}

	/**
	 * Save or update document
	 * @param directory
	 * 				The directory to save or to update
	 */
	public static synchronized void saveOrUpdate(GedDirectory directory)
	{
		logger.debug("save directory");
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.saveOrUpdate(directory);
		session.getTransaction().commit();
		session.close();
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
	public static synchronized void updateDirectoryPath(String oldName, String newName) {
		
		logger.debug("Rename : " + oldName + " to " + newName);

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(GedDirectory.class).add(Restrictions.eq("relativeDirectoryPath", oldName));  
		
		@SuppressWarnings("unchecked")
		List<GedDirectory> results = criteria.list();  
		
		for (GedDirectory directory : results) {
			directory.setRelativeDirectoryPath(directory.getRelativeDirectoryPath().replaceFirst(oldName, newName));
			session.update(directory);
		}
		
		session.getTransaction().commit();
		session.close();
	}

	
	/**
	 * Delete a document file
	 * 
	 * If some document as not at least on attached file after delete, it's removed
	 */
	public static synchronized void deleteDirectory(String directoryPath) {
		logger.debug("Remove directory : " + directoryPath);
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(GedDirectory.class).add(Restrictions.eq("relativeDirectoryPath", directoryPath + "%"));  
		
		@SuppressWarnings("unchecked")
		List<GedDirectory> results = criteria.list();  
		
		for (GedDirectory directory : results) {
			session.delete(directory);
		}
		
		session.getTransaction().commit();
		session.close();
	}
}
