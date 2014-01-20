package fr.simple.ged.common.dto;

import java.io.Serializable;

import fr.simple.ged.common.GedNodeType;

public class GedLibraryDto extends GedNodeDto implements Serializable {

	private String name;
	
	private String description;

	
	public GedLibraryDto() {
		super(GedNodeType.LIBRARY);
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
