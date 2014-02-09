package fr.simple.ged.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.ged.model.GedDirectory;


public interface GedDirectoryRepository extends ElasticsearchRepository<GedDirectory, String> {

	List<GedDirectory> findByParentId(String parentId);
	
	List<GedDirectory> findByName(String name);
	
}
