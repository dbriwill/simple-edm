package fr.simple.ged.model;

import org.springframework.data.elasticsearch.annotations.Document;

import fr.simple.ged.common.GedNodeType;

@Document(indexName = "documents", type = "file")
public class GedFile extends GedNode {

	private String fileName;
	
	public GedFile() {
		super(GedNodeType.FILE);
	}
	
	public GedFile(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
