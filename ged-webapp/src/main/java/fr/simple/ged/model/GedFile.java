package fr.simple.ged.model;

import java.io.Serializable;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "documents", type = "file")
public class GedFile implements Serializable {

	private String fileName;
	
	public GedFile() {
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
