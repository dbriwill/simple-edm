package fr.simple.ged.common.dto;

import java.io.Serializable;

import fr.simple.ged.common.GedNodeType;

/**
 * Some ged node is something in the ged tree
 * @author xavier
 *
 */
public class GedNodeDto implements Serializable {

	private String id;

	private GedNodeType gedNodeType;
	
	private GedNodeDto parent;
	
	private String name;
	
	public GedNodeDto() {
	    // please give me a node type... Or you'll do nothing with me !
	}
	
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
}

