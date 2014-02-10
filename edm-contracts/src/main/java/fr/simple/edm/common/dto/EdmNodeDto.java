package fr.simple.edm.common.dto;

import java.io.Serializable;

import fr.simple.edm.common.EdmNodeType;

/**
 * Some edm node is something in the edm tree
 * @author xavier
 *
 */
public class EdmNodeDto implements Serializable {

	private String id;

	private EdmNodeType edmNodeType;
	
	private EdmNodeDto parent;
	
	private String name;
	
	public EdmNodeDto() {
	    // please give me a node type... Or you'll do nothing with me !
	}
	
	public EdmNodeDto(EdmNodeType edmNodeType) {
		this.edmNodeType = edmNodeType;
	}
	
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public EdmNodeType getEdmNodeType() {
		return edmNodeType;
	}

	public void setEdmNodeType(EdmNodeType edmNodeType) {
		this.edmNodeType = edmNodeType;
	}

	public EdmNodeDto getParent() {
		return parent;
	}

	public void setParent(EdmNodeDto parent) {
		this.parent = parent;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
}

