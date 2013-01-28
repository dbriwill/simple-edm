package com.simple.ged.connector.plugins.dto;

import java.io.File;
import java.util.Date;

/**
 * 
 * This class represent a leaf in the GED tree : some file
 * 
 * @author xavier
 *
 */
public class GedDocumentDTO extends GedComponentDTO {

	/**
	 * Path to the manipulated file
	 */
	private File file;

    /**
     * The document name
     */
	private String documentName;

    /**
     * The document description
     */
    private String documentDescription;

    /**
     * The document date
     */
    private Date documentDate;


	public GedDocumentDTO(String relativePathToRoot) {
		super(relativePathToRoot);
		this.file = new File(relativePathToRoot);
	}

	/**
	 * 
	 */
	public File getFile() {
		return file;
	}


	@Override
	protected void persist() {
		// TODO implement me !
	}


    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }
}
