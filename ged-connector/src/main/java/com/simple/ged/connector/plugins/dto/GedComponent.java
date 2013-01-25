package com.simple.ged.connector.plugins.dto;

/**
 * 
 * This is an abstract component of the GED, which can be file or directory
 * 
 * @author xavier
 *
 */
public abstract class GedComponent {
	
	/**
	 * The relative path to the document root
	 */
	private String relativePathToRoot;

	
	protected GedComponent(String relativePathToRoot) {
		this.relativePathToRoot = relativePathToRoot;
	}
	
	/**
	 * Update this component !
	 */
	public abstract void update();
	
	
	public String getRelativePathToRoot() {
		return relativePathToRoot;
	}

	public void setRelativePathToRoot(String relativePathToRoot) {
		this.relativePathToRoot = relativePathToRoot;
	}
	
}
