package com.simple.ged.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.simple.ged.models.GedDocument;

/**
 * 
 * See spring namedQuery for more information : 
 * http://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/jpa.repositories.html#jpa.query-methods.named-queries
 * 
 * @author xavier
 * 
 */
public interface GedDocumentRepository extends JpaRepository<GedDocument, Integer> {

	//@Query("select gd from GedDocument gd join GedDocumentFile gdf where gdf.relativeFilePath = %?1")
	//public GedDocument findByRelativeFilePath(String relativeFilePath);

    public GedDocument findByDocumentFilesRelativeFilePath(String relativeFilePath);
}
