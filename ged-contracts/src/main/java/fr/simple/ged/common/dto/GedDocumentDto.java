package fr.simple.ged.common.dto;

import java.io.Serializable;

import fr.simple.ged.common.GedNodeType;

public class GedDocumentDto extends GedNodeDto implements Serializable {

	private String description;

	public GedDocumentDto() {
		super(GedNodeType.DOCUMENT);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
