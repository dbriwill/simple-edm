package com.simple.ged.connector.plugins.dto;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.connector.plugins.dto.listeners.GedDocumentDtoDataSourceListener;

/**
 * 
 * This class represent a leaf in the GED tree : some file
 * 
 * @author xavier
 *
 */
public class GedDocumentDTO extends GedComponentDTO {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(GedDocumentDTO.class);
	
	/**
	 * My communicator with the Ged Core, will be injected by the core
	 */
	private GedDocumentDtoDataSourceListener gedDocumentDtoDataSourceListener;
	
	/**
	 * Path to the manipulated file
	 */
	private File file = null;

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
	}

	/**
	 * The file, loaded only if necessary
	 */
	public File getFile() {
		if (file == null) {
			file = new File(getFilePathToLibraryRoot() + getRelativePathToRoot());
		}
		return file;
	}
	
	/**
	 * What is the absolute file path to library root ?
	 * 
	 * For exemple, will return : "/home/xavier/Documents/"
	 */
	public String getFilePathToLibraryRoot() {
		return gedDocumentDtoDataSourceListener.getFilePathToLibraryRoot();
	}


	@Override
	protected void persist() {
		// TODO implement me !
		logger.error("Not implemented yet ! You want to help me or your need this feature ? Implement this for me please !");
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

	public GedDocumentDtoDataSourceListener getGedDocumentDtoDataSourceListener() {
		return gedDocumentDtoDataSourceListener;
	}

	public void setGedDocumentDtoDataSourceListener(
			GedDocumentDtoDataSourceListener gedDocumentDtoDataSourceListener) {
		this.gedDocumentDtoDataSourceListener = gedDocumentDtoDataSourceListener;
	}

}
