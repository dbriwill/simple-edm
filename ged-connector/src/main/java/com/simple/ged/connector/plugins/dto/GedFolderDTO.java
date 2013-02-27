package com.simple.ged.connector.plugins.dto;

import com.simple.ged.connector.plugins.dto.listeners.GedFoldertDtoDataSourceListener;

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
    private GedFoldertDtoDataSourceListener gedFoldertDtoDataSourceListener;


	public GedFolderDTO(String relativePathToRoot) {
		super(relativePathToRoot);
		this.children = null;
	}

	public List<GedComponentDTO> getChildren() {
        if (children == null) {
            children = gedFoldertDtoDataSourceListener.loadAndGiveMeMyChildren();
        }
		return children;
	}


	@Override
	protected void persist() {
		// TODO implement me !
		logger.error("Not implemented yet ! You want to help me or your need this feature ? Implement this for me please !");
	}



    //

    public GedFoldertDtoDataSourceListener getGedFoldertDtoDataSourceListener() {
        return gedFoldertDtoDataSourceListener;
    }

    public void setGedFoldertDtoDataSourceListener(GedFoldertDtoDataSourceListener gedFoldertDtoDataSourceListener) {
        this.gedFoldertDtoDataSourceListener = gedFoldertDtoDataSourceListener;
    }
}
