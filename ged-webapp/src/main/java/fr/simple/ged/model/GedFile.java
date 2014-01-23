package fr.simple.ged.model;

import org.springframework.data.elasticsearch.annotations.Document;

import fr.simple.ged.common.GedNodeType;

@Document(indexName = "documents", type = "file")
public class GedFile extends GedNode {

	public GedFile() {
		super(GedNodeType.FILE);
	}
	
	public GedFile(String fileName) {
		super(GedNodeType.FILE);
		super.setName(fileName);
	}
	
}
