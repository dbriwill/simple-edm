package fr.simple.ged.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.ged.model.GedDocument;


public interface GedDocumentRepository extends ElasticsearchRepository<GedDocument, String> {

	List<GedDocument> findByParentId(String parentId);

	List<GedDocument> findByName(String name);
	
}
