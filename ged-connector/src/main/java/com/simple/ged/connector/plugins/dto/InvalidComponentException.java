package com.simple.ged.connector.plugins.dto;

/**
 * 
 * Exception when you deal with something which is not possible...
 * 
 * @author xavier
 *
 */
public class InvalidComponentException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7927548467981140241L;

	public InvalidComponentException(String message, Throwable e) {
		super(message, e);
	}
}
