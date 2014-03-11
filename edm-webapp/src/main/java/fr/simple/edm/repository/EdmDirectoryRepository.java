package fr.simple.edm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.edm.model.EdmDirectory;


public interface EdmDirectoryRepository extends ElasticsearchRepository<EdmDirectory, String> {

	Page<EdmDirectory> findByParentId(String parentId, Pageable page);
	
	List<EdmDirectory> findByName(String name);
	
}
