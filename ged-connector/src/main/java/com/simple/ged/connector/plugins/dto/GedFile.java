package com.simple.ged.connector.plugins.dto;

import java.io.File;

/**
 * 
 * This class represent a leaf in the GED tree : some file
 * 
 * @author xavier
 *
 */
public class GedFile extends GedComponent {

	/**
	 * Path to the manipulated file
	 */
	private File file;

	
	public GedFile(String relativePathToRoot) {
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
	public void update() {
		// TODO implement me !
	}
}
