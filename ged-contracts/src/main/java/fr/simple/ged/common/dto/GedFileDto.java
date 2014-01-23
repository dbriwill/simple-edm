package fr.simple.ged.common.dto;

import java.io.Serializable;

import fr.simple.ged.common.GedNodeType;

public class GedFileDto extends GedNodeDto implements Serializable {

	private String fileName;
	
	public GedFileDto() {
		super(GedNodeType.FILE);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
