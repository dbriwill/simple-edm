package fr.simple.edm.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.edm.model.EdmDirectory;


public interface EdmDirectoryRepository extends ElasticsearchRepository<EdmDirectory, String> {

	List<EdmDirectory> findByParentId(String parentId);
	
	List<EdmDirectory> findByName(String name);
	
}
