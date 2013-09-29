package com.simple.ged.connector.plugins.dto;

/**
 * 
 * Public exposition of GedPluginProperty
 * 
 * @author Xavier
 *
 */
public class SimpleGedPluginPropertyDTO {

	private String propertyKey;
	

	private String propertyLabel;


	private String propertyValue;


	private Boolean isBooleanProperty;
	

	private Boolean booleanValue;
	

	private boolean hidden;
	
	
	public SimpleGedPluginPropertyDTO() {
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
