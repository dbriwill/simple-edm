package fr.simple.edm.model;

import org.springframework.data.elasticsearch.annotations.Document;

import fr.simple.edm.common.EdmNodeType;

@Document(indexName = "documents", type = "library", shards = 1, replicas = 0)
public class EdmLibrary extends EdmNode {

	private String description;

	public EdmLibrary() {
		super(EdmNodeType.LIBRARY);
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
