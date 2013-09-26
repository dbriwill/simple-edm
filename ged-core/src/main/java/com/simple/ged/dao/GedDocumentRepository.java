package com.simple.ged.dao;

import org.springframework.data.jpa.repository.JpaRepository;

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


    public GedDocument findByDocumentFilesRelativeFilePath(String relativeFilePath);
    
}
