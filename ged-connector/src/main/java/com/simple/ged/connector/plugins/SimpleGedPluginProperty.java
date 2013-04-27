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
	 * The value IS a boolean ?
	 */
	private Boolean isBooleanProperty;
	
	/**
	 * The value as boolean (if valid for this property)
	 */
	private Boolean booleanValue;
	
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

	public boolean getBooleanValue() {
		return booleanValue != null ? booleanValue : false;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public boolean isBooleanProperty() {
		return isBooleanProperty != null ? isBooleanProperty : false;
	}
	public boolean getIsBooleanProperty() {
		return isBooleanProperty();
	}
	
	public void setBooleanProperty(Boolean isBooleanProperty) {
		this.isBooleanProperty = isBooleanProperty;
	}
	public void setIsBooleanProperty(Boolean isBooleanProperty) {
		this.isBooleanProperty = isBooleanProperty;
	}
	
}

