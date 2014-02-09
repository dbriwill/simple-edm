package fr.simple.ged.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.ged.model.GedLibrary;


public interface GedLibraryRepository extends ElasticsearchRepository<GedLibrary, String> {

    List<GedLibrary> findByName(String name);
    
}
