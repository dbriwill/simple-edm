package fr.simple.ged.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import fr.simple.ged.model.GedNode;

public interface GedNodeRepository extends ElasticsearchRepository<GedNode, String> {

}
