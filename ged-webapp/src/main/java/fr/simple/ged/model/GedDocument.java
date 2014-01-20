package fr.simple.ged.model;

import javax.persistence.DiscriminatorValue;

import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

import fr.simple.ged.common.GedNodeType;

@Document(indexName = "documents", type = "document")
@DiscriminatorValue("document")
public class GedDocument extends GedNode {

	@Version
    private Long version;
	
	private String name;
	
	private String description;

	
	public GedDocument() {
		super(GedNodeType.DOCUMENT);
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
