package com.simple.ged.connector.plugins;


/**
 * A property is a value that the user can define for the plugin
 * 
 * For example, login or password.
 * 
 * The key is not an attribute because we used a map (see below)
 * 
 * @author xavier
 */
public class SimpleGedPluginProperty {
	
    /**
     * ID (for mapping)
     */
    private int id;
	
	/**
	 * The property key
	 */
	private String propertyKey;
	
	/**
	 * Property label (for popup dialog)
	 */
	private String propertyLabel;
	
	/**
	 * The value defined by the user
	 */
	private String propertyValue;

	/**
	 * Is hidden property ? Like passwords
	 */
	private boolean hidden;
	
	
	public SimpleGedPluginProperty() {
		hidden = false;
	}
	
	public String getPropertyLabel() {
		return propertyLabel;
	}

	public void setPropertyLabel(String propertyLabel) {
		this.propertyLabel = propertyLabel;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public boolean isHidden() {
		return hidden;
	}
	
	public boolean getHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

