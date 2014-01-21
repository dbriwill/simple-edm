package fr.simple.ged.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.simple.ged.common.GedNodeType;

@Entity
@Document(indexName = "documents", type = "directory")
@DiscriminatorValue("GedDirectory")
public class GedDirectory extends GedNode {
	
	@Version
    private Long version;
	
	private String name;
	
	private String description;

	
	public GedDirectory() {
		super(GedNodeType.DIRECTORY);
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
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
