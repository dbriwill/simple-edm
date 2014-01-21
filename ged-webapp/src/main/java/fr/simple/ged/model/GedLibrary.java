package fr.simple.ged.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.simple.ged.common.GedNodeType;

@Entity
@Document(indexName = "documents", type = "library")
@DiscriminatorValue("GedLibrary")
public class GedLibrary extends GedNode {

	private String name;

	private String description;

	
	public GedLibrary() {
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
