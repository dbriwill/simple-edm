package com.simple.ged.connector.plugins.dto.listeners;

/**
 * User: Xavier
 *
 * This class is a bridge between the core and actions needed by connector
 */
public interface GedComponentDtoDataSourceListener {
	
	/**
	 * What is the absolute file path to library root ?
	 * 
	 * For exemple, will return : "/home/xavier/Documents/"
	 */
	public String getFilePathToLibraryRoot();
	
}

