package fr.simple.ged.model;

import org.springframework.data.elasticsearch.annotations.Document;

import fr.simple.ged.common.GedNodeType;

@Document(indexName = "documents", type = "directory")
public class GedDirectory extends GedNode {
	
	private String description;

	public GedDirectory() {
		super(GedNodeType.DIRECTORY);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
