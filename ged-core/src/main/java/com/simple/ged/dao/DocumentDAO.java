package com.simple.ged.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.simple.ged.models.GedDocument;
import com.simple.ged.models.GedDocumentFile;
import com.simple.ged.models.GedDocumentPhysicalLocation;

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
    public static synchronized GedDocument findDocumentbyId(Integer id) {
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
	
	
	/**
	 * Get documents with the given location
	 */
	public static List<GedDocument> findDocumentbyLocation(GedDocumentPhysicalLocation location) {
		Session session = HibernateUtil.getSessionFactory().openSession();  
		
		Criteria criteria = session.createCriteria(GedDocument.class)
				.add(Restrictions.eq("location", location));  
		
		@SuppressWarnings("unchecked")
		List<GedDocument> results = criteria.list();  
		
		session.close();
		
		return results;
	}
	
	
	/**
	 * Search document which match with one of the given
	 */
	@SuppressWarnings("unchecked")
	public static synchronized List<GedDocumentFile> getDocumentWhichContainsEveryWords(List<String> words) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		List<GedDocumentFile> results =  new ArrayList<>();
		
		// 1. Documents name which match with pattern
		Criteria criteriaDocumentName = session.createCriteria(GedDocument.class);
		
		for (String word : words) {
			criteriaDocumentName.add(Restrictions.like("name", "%" + word + "%").ignoreCase());
		}
		
		// add founded documents
		List<GedDocument> docsWhichMatchWithName = criteriaDocumentName.list();
		for (GedDocument doc : docsWhichMatchWithName) {
			for (GedDocumentFile file : doc.getDocumentFiles()) {
				results.add(file);
			}
		}
		
		// 2. Document description match with pattern
		Criteria criteriaDocumentDesc = session.createCriteria(GedDocument.class);
		
		for (String word : words) {
			criteriaDocumentDesc.add(Restrictions.like("description", "%" + word + "%").ignoreCase());
		}
		// add founded documents
		List<GedDocument> docsWhichMatchWithDescription = criteriaDocumentDesc.list();
		for (GedDocument doc : docsWhichMatchWithDescription) {
			for (GedDocumentFile file : doc.getDocumentFiles()) {
				results.add(file);
			}
		}

		// 3. Documents files which match with pattern
		Criteria criteriaFile = session.createCriteria(GedDocumentFile.class);
		for (String word : words) {
			criteriaFile.add(Restrictions.like("relativeFilePath", "%" + word + "%").ignoreCase());
		}
		
		// add founded files
		List<GedDocumentFile> files = criteriaFile.list();  
		for (GedDocumentFile file : files) {
			results.add(file);
		}
		
		session.close();
		
		return results;
	}
	
}
