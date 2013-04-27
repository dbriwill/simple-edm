package com.simple.ged.connector.plugins.dto;

import com.simple.ged.connector.plugins.dto.listeners.GedFolderDtoDataSourceListener;
import com.simple.ged.connector.plugins.feedback.SimpleGedPluginException;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * This class represent a composite in the GED tree : some directory
 * 
 * @author xavier
 *
 */
public class GedFolderDTO extends GedComponentDTO {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(GedFolderDTO.class);
	
	/**
	 * Children list
     *
     * The value is null if children are not loaded
	 */
	private List<GedComponentDTO> children;

    /**
     * Someone is listening for what I need
     */
    private GedFolderDtoDataSourceListener gedFoldertDtoDataSourceListener;


	public GedFolderDTO(String relativePathToRoot) {
		super(relativePathToRoot);
		this.children = null;
	}

	public List<GedComponentDTO> getChildren() {
		try {
	        if (children == null) {
	            children = gedFoldertDtoDataSourceListener.loadAndGiveMeMyChildren(this);
	        }
		}
		catch (Exception e) {
			try {
				throw new SimpleGedPluginException("Cannot get children !", e);
			} catch (SimpleGedPluginException e1) {
				logger.error("Cannot get children from DTO : {}", e1);
			}
		}
		return children;
	}

	
	/**
	 * What is the absolute file path to library root ?
	 * 
	 * For exemple, will return : "/home/xavier/Documents/"
	 */
	public String getFilePathToLibraryRoot() {
		return gedFoldertDtoDataSourceListener.getFilePathToLibraryRoot();
	}
	

	@Override
	protected void persist() {
		// TODO implement me !
		logger.error("Not implemented yet ! You want to help me or your need this feature ? Implement this for me please !");
	}



    //

    public GedFolderDtoDataSourceListener getGedFoldertDtoDataSourceListener() {
        return gedFoldertDtoDataSourceListener;
    }

    public void setGedFoldertDtoDataSourceListener(GedFolderDtoDataSourceListener gedFoldertDtoDataSourceListener) {
        this.gedFoldertDtoDataSourceListener = gedFoldertDtoDataSourceListener;
    }
}
