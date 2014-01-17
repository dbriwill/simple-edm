package fr.simple.ged.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.ged.model.GedDirectory;
import org.springframework.stereotype.Repository;

//@Repository
public interface GedDirectoryRepository extends ElasticsearchRepository<GedDirectory, String> {

}
