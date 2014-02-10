package fr.simple.edm.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.edm.model.EdmDocument;


public interface EdmDocumentRepository extends ElasticsearchRepository<EdmDocument, String> {

	List<EdmDocument> findByParentId(String parentId);

	List<EdmDocument> findByName(String name);
	
}
