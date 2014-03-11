package fr.simple.edm.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.edm.model.EdmLibrary;


public interface EdmLibraryRepository extends ElasticsearchRepository<EdmLibrary, String> {

    List<EdmLibrary> findByName(String name);
    
}
