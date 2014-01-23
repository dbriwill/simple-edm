package fr.simple.ged.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

import fr.simple.ged.common.GedNodeType;

@Document(indexName = "documents", type = "document")
public class GedDocument extends GedNode {

	@Version
    private Long version;
	
	private String description;

	private List<GedFile> files; 
	
	public GedDocument() {
		super(GedNodeType.DOCUMENT);
		files = new ArrayList<>();
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<GedFile> getFiles() {
		return files;
	}

	public void setFiles(List<GedFile> files) {
		this.files = files;
	}

	public void addFile(GedFile gedFile) {
		files.add(gedFile);
	}
}
