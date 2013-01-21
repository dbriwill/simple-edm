package com.simple.ged.connector.plugins.getter;

/**
 * This exception should be throwed by the plugin when something is wrong
 * 
 * @author xavier
 */
public class SimpleGedPluginException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public SimpleGedPluginException(String errorMessage) {
		super(errorMessage);
	}
}
