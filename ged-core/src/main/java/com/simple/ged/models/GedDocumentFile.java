package com.simple.ged.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;



/**
 * Some document file
 * @author xavier
 *
 */
@Entity
@Table(name="document_file")
public class GedDocumentFile {

	/**
	 * The ID
	 */
    @Id
    @GeneratedValue
    @Column(name="rowid")
	private Integer id;
	
	/**
	 * The file path, relative to the library root
	 */
    @Column(name="path")
    @Index(name="idx_doc_file_path")
	private String relativeFilePath;

	/**
	 * Document
	 */
    @ManyToOne
    @JoinColumn(name="document_id", insertable=false, updatable=false, nullable=false)
	private GedDocument document;
	
	
	private GedDocumentFile() {
		id = null;
		document = null;
		relativeFilePath = "";
	}
	
	
	public GedDocumentFile(String relativeFilePath) {
		this();
		this.relativeFilePath = relativeFilePath;
	}

	/**
	 * Get the file path relative to library root
	 */
	public String getRelativeFilePath() {
		return relativeFilePath;
	}
	
	/**
	 * Define (or re-define) the file path
	 * 
	 * @param relativeFilePath
	 * 				The RELATIVE file path
	 */
	public void setRelativeFilePath(String relativeFilePath) {
		this.relativeFilePath = relativeFilePath;
	}

	public String toString() {
		return relativeFilePath;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public GedDocument getDocument() {
		return document;
	}

	public void setDocument(GedDocument document) {
		this.document = document;
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
		GedDocumentFile other = (GedDocumentFile) obj;
        return id == other.id;
    }
}
