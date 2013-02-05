package com.simple.ged.connector.plugins.dto;

import com.simple.ged.connector.plugins.dto.listeners.GedFoldertDtoDataSourceListener;

import java.util.List;

/**
 * 
 * This class represent a composite in the GED tree : some directory
 * 
 * @author xavier
 *
 */
public class GedFolderDTO extends GedComponentDTO {

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
	}



    //

    public GedFoldertDtoDataSourceListener getGedFoldertDtoDataSourceListener() {
        return gedFoldertDtoDataSourceListener;
    }

    public void setGedFoldertDtoDataSourceListener(GedFoldertDtoDataSourceListener gedFoldertDtoDataSourceListener) {
        this.gedFoldertDtoDataSourceListener = gedFoldertDtoDataSourceListener;
    }
}
