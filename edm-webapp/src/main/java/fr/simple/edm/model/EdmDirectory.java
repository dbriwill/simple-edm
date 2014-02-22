package fr.simple.edm.model;

import org.springframework.data.elasticsearch.annotations.Document;

import fr.simple.edm.common.EdmNodeType;

@Document(indexName = "documents", type = "directory", shards = 1, replicas = 0)
public class EdmDirectory extends EdmNode {
	
	private String description;

	public EdmDirectory() {
		super(EdmNodeType.DIRECTORY);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
