package fr.simple.ged.common.dto;

import java.io.Serializable;

import fr.simple.ged.common.GedNodeType;

public class GedLibraryDto extends GedNodeDto implements Serializable {

	private String description;

	public GedLibraryDto() {
		super(GedNodeType.LIBRARY);
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
