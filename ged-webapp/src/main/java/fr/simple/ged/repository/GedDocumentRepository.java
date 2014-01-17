package fr.simple.ged.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.ged.model.GedDocument;


public interface GedDocumentRepository extends ElasticsearchRepository<GedDocument, String> {

}
