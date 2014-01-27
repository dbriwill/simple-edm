package fr.simple.ged.common.dto;

import java.io.Serializable;

import fr.simple.ged.common.GedNodeType;

public class GedFileDto implements Serializable {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
