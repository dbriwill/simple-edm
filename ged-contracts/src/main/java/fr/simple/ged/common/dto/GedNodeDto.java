package fr.simple.ged.common.dto;

import fr.simple.ged.common.GedNodeType;

/**
 * Some ged node is something in the ged tree
 * @author xavier
 *
 */
public class GedNodeDto {

	private String id;

	private GedNodeType gedNodeType;
	
	private GedNodeDto parent;
	
	
	public GedNodeDto(GedNodeType gedNodeType) {
		this.gedNodeType = gedNodeType;
	}
	
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public GedNodeType getGedNodeType() {
		return gedNodeType;
	}

	public void setGedNodeType(GedNodeType gedNodeType) {
		this.gedNodeType = gedNodeType;
	}

	public GedNodeDto getParent() {
		return parent;
	}

	public void setParent(GedNodeDto parent) {
		this.parent = parent;
	}
}

