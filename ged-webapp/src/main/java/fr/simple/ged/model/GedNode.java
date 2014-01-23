package fr.simple.ged.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.drew.lang.annotations.NotNull;

import fr.simple.ged.common.GedNodeType;

/**
 * Some ged node is something in the ged tree
 * @author xavier
 *
 */
@MappedSuperclass
public class GedNode implements Serializable {

	@Id
	private String id;
	
	@NotNull
	private GedNodeType gedNodeType;
	
	private GedNode parent;
	
	public GedNode(GedNodeType gedNodeType) {
		this.gedNodeType = gedNodeType;
	}
	
	public GedNode() {
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

	public GedNode getParent() {
		return parent;
	}

	public void setParent(GedNode parent) {
		this.parent = parent;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((gedNodeType == null) ? 0 : gedNodeType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GedNode other = (GedNode) obj;
		if (gedNodeType != other.gedNodeType)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
