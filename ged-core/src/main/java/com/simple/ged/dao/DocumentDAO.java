package com.simple.ged.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.models.GedDocument;
import com.simple.ged.models.GedDocumentFile;

import fr.xmichel.toolbox.hibernate.sqlite.HibernateUtil;

/**
 * The document manager is an interface for current manipulations on a ged
 * document
 * 
 * In two words, it's a DAO interface
 * 
 * Note : all manipulate path are relative path to library root
 * 
 * @author xavier
 * 
 */
public final class DocumentDAO {

	
	private static final Logger logger = LoggerFactory.getLogger(DocumentDAO.class);
	
	/**
	 * Should not be instantiated
	 */
	private DocumentDAO() {
	}


    /**
     * Get messages, sorted by date desc
     */
    public static synchronized List<GedDocument> getAllGedDocuments() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("FROM GedDocument");

        @SuppressWarnings("unchecked")
        List<GedDocument> results = query.list();

        session.close();

        return results;
    }

	
	/**
	 * 
	 * @param filePath
	 *            The file path, relative to ged root
	 */
	public static synchronized GedDocument findDocumentbyFilePath(String filePath) {
		logger.debug("Get document for file : {}", filePath);

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(GedDocumentFile.class).add(Restrictions.eq("relativeFilePath", filePath));  
		
		@SuppressWarnings("unchecked")
		List<GedDocumentFile> results = criteria.list();  

		GedDocument d = null;

		if ( ! results.isEmpty() ) {
			d = results.get(0).getDocument();
		}
		
		session.close();
		
		return d;
	}


    /**
     * @param id
     *            The document id
     */
    public static synchronized GedDocument find(Integer id) {
        logger.debug("Get document for id : {}", id);

        Session session = HibernateUtil.getSessionFactory().openSession();
        GedDocument d = (GedDocument)session.get(GedDocument.class, id);
        session.close();

        return d;
    }


	/**
	 * Save or update document
	 * @param document
	 * 				The document to save or to update
	 */
	public static synchronized void saveOrUpdate(GedDocument document)
	{
		logger.debug("save document");
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.saveOrUpdate(document);
		session.getTransaction().commit();
		session.close();
	}
	

	/**
	 * Rename a document or a directory (same treatment)
	 * 
	 * @param oldName
	 *            The old name contains the relative path to file
	 * 
	 * @param newName
	 *            The new name contains the relative path to file, with the new
	 *            name =)
	 */
	public static synchronized void updateFilePath(String oldName, String newName) {
		
		logger.debug("Rename : " + oldName + " to " + newName);

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(GedDocumentFile.class).add(Restrictions.like("relativeFilePath", oldName + "%"));  
		
		@SuppressWarnings("unchecked")
		List<GedDocumentFile> results = criteria.list();  
		
		for (GedDocumentFile file : results) {
			file.setRelativeFilePath(file.getRelativeFilePath().replaceFirst(oldName, newName));
			session.update(file);
		}
		
		session.getTransaction().commit();
		session.close();
	}

	
	/**
	 * Delete a document file
	 * 
	 * If some document as not at least on attached file after delete, it's removed
	 */
	public static synchronized void deleteFile(String filePath) {
		logger.debug("Remove document : " + filePath);
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(GedDocumentFile.class).add(Restrictions.like("relativeFilePath", filePath + "%"));  
		
		@SuppressWarnings("unchecked")
		List<GedDocumentFile> results = criteria.list();  
		
		for (GedDocumentFile file : results) {
		
			Criteria c = session.createCriteria(GedDocumentFile.class).add(Restrictions.eq("relativeFilePath", file.getRelativeFilePath())); 
			@SuppressWarnings("unchecked")
			List<GedDocumentFile> r = c.list();  
			
			for (GedDocumentFile f : r) {
				f.getDocument().removeFile(f);
				session.save(f.getDocument());
				
				if (f.getDocument().getDocumentFiles().isEmpty()) {
					session.delete(f.getDocument());
				}
			}
			
		}
		
		session.getTransaction().commit();
		session.close();
	}
	
}
