package fr.simple.ged.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.ged.model.GedDocument;
import org.springframework.stereotype.Repository;

//@Repository
public interface GedDocumentRepository extends ElasticsearchRepository<GedDocument, String> {

}
