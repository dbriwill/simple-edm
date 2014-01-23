package fr.simple.ged.model;

import org.springframework.data.elasticsearch.annotations.Document;

import fr.simple.ged.common.GedNodeType;

@Document(indexName = "documents", type = "library")
public class GedLibrary extends GedNode {

	private String description;

	public GedLibrary() {
		super(GedNodeType.LIBRARY);
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
