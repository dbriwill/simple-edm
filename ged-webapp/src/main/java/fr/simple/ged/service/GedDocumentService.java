package fr.simple.ged.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.simple.ged.model.GedDocument;
import fr.simple.ged.repository.GedDocumentRepository;

@Service
public class GedDocumentService {

    private final Logger logger = LoggerFactory.getLogger(GedDocumentService.class);

    @Inject
	private GedDocumentRepository gedDocumentRepository;
	
    public GedDocument findOne(String id) {
    	return gedDocumentRepository.findOne(id);
    }
    
	public GedDocument save(GedDocument gedDocument) {
		// TODO [improve me] ; see https://github.com/spring-projects/spring-data-elasticsearch/issues/21 and https://github.com/spring-projects/spring-data-elasticsearch/pull/27
		// unless it's fixed, I set my generated ID
		if (gedDocument.getId() == null || gedDocument.getId().isEmpty()) {
			gedDocument.setId(String.valueOf(System.currentTimeMillis()));
		}
		return gedDocumentRepository.index(gedDocument);
	}
}
