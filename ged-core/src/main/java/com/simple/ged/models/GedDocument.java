package com.simple.ged.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;


/**
 * Some document of the GED
 * @author xavier
 *
 */
@Entity
@Table(name="document")
public class GedDocument implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
     * Document ID
     */
    @Id
    @GeneratedValue
    @Column(name="rowid")
    private Integer id;
	
	/**
	 * Files associated to document
	 */
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderColumn(name="file_order")
    @JoinColumn(name="document_id", nullable=false)
    private List<GedDocumentFile> documentFiles;  

    /**
     * Document name
     */
    @Column(name="name")
    private String name;

    /**
     * Document date
     */
    @Column(name="date")
    private Date date;

    /**
     * Document description
     */
    @Column(name="description")
    private String description;
    
    /**
     * Document location
     */
    @ManyToOne
    @JoinColumn(name="location", nullable=true)
    private GedDocumentPhysicalLocation location;



	public GedDocument(int id, List<GedDocumentFile> documentFiles, String name, Date date, String description) {
		this.id = id;
		this.documentFiles = documentFiles;
		this.name = name;
		this.date = date;
		this.description = description;
		this.location = null;
	}

	public GedDocument(int id, List<GedDocumentFile> documentFiles, String name, Date date, String description, GedDocumentPhysicalLocation location) {
		this.id = id;
		this.documentFiles = documentFiles;
		this.name = name;
		this.date = date;
		this.description = description;
		this.location = location;
	}
	

	public GedDocument() {
		this.id = null;
		this.documentFiles = new ArrayList<>();
		this.name = "";
		this.date = new Date();
		this.description = "";
	}


	public List<GedDocumentFile> getDocumentFiles() {
		return documentFiles;
	}


	public void setDocumentFiles(List<GedDocumentFile> documentFiles) {
		this.documentFiles = documentFiles;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}    
	
	public void addFile(GedDocumentFile file) {
		documentFiles.add(file);
	}
	
	public String toString() {
		String ret = "";
		
		ret += "  Name : " + name;
		ret += " ,date : " + date;
		ret += " ,desc : " + description;
		ret += " ,files :";
		
		for (GedDocumentFile file : documentFiles) {
			ret += file.toString();
		}
		
		return ret;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	
	
	public GedDocumentPhysicalLocation getLocation() {
		return location;
	}

	public void setLocation(GedDocumentPhysicalLocation location) {
		this.location = location;
	}

	
	/**
	 * Remove a file from the document
	 */
	public void removeFile(GedDocumentFile file) {
		documentFiles.remove(file);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GedDocument other = (GedDocument) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
