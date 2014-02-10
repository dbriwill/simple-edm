package fr.simple.edm.model;

import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

import fr.simple.edm.common.EdmNodeType;

@Document(indexName = "documents", type = "document")
public class EdmDocument extends EdmNode {

	@Version
    private Long version;
	
	private String description;

	private String filename; 
	
	public EdmDocument() {
		super(EdmNodeType.DOCUMENT);
		filename = "";
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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
