package fr.simple.ged.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.ged.model.GedFile;

public interface GedFileRepository extends ElasticsearchRepository<GedFile, String> {

}
