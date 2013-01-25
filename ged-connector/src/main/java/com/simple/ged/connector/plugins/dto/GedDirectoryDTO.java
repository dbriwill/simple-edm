package com.simple.ged.connector.plugins.dto;

import java.util.List;

/**
 * 
 * This class represent a composite in the GED tree : some directory
 * 
 * @author xavier
 *
 */
public class GedDirectoryDTO extends GedComponentDTO {

	/**
	 * Children list
	 */
	private List<GedComponentDTO> children;
	
	
	public GedDirectoryDTO(String relativePathToRoot, List<GedComponentDTO> children) {
		super(relativePathToRoot);
		this.children = children;
	}

	public List<GedComponentDTO> getChildren() {
		return children;
	}

	@Override
	protected void update() {
		// TODO implement me !
	}
}
