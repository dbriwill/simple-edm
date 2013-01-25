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

	
	public void addChild(GedComponentDTO child) {
		// TODO implement me !
	}
	
	public void removeChild(GedComponentDTO child) {
		// TODO implement me !
	}
	
	/**
	 * 
	 * @param child
	 *
	 * @warn The child comparaison is based on relative file path to root
	 */
	public void updateChild(GedComponentDTO child) {
		// TODO implement me !
	}
	
	@Override
	public void update() {
		// TODO implement me !
	}
}
