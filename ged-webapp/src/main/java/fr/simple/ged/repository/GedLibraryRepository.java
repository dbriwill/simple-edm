package fr.simple.ged.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.ged.model.GedLibrary;
import org.springframework.stereotype.Repository;

//@Repository
public interface GedLibraryRepository extends ElasticsearchRepository<GedLibrary, String> {

}
