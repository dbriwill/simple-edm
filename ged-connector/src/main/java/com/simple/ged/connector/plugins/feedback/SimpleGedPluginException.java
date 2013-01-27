package com.simple.ged.connector.plugins.feedback;

/**
 * This exception should be thrown by the plugin when something is wrong
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
