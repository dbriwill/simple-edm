package com.simple.ged.connector.plugins.data;

import java.util.List;

/**
 * 
 * This class represent a composite in the GED tree : some directory
 * 
 * @author xavier
 *
 */
public class GedDirectory extends GedComponent {

	/**
	 * Children list
	 */
	private List<GedComponent> children;
	
	
	public GedDirectory(String relativePathToRoot, List<GedComponent> children) {
		super(relativePathToRoot);
		this.children = children;
	}

	
	public void addChild(GedComponent child) {
		// TODO implement me !
	}
	
	public void removeChild(GedComponent child) {
		// TODO implement me !
	}
	
	/**
	 * 
	 * @param child
	 *
	 * @warn The child comparaison is based on relative file path to root
	 */
	public void updateChild(GedComponent child) {
		
	}
	
	@Override
	public void update() {
		// TODO implement me !
	}
}
