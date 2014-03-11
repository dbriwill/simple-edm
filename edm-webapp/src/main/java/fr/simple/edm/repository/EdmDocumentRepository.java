package fr.simple.edm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.edm.model.EdmDocument;


public interface EdmDocumentRepository extends ElasticsearchRepository<EdmDocument, String> {

    Page<EdmDocument> findByParentId(String parentId, Pageable page);

	List<EdmDocument> findByName(String name);
	
}
