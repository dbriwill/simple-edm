package com.simple.ged.models;

import com.simple.ged.connector.plugins.dto.SimpleGedPluginPropertyDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * A property is a value that the user can define for the plugin
 * 
 * For example, login or password.
 * 
 * The key is not an attribute because we used a map (see below)
 * 
 * @author xavier
 */
@Entity
@Table(name="plugin_property")
public class GedPluginProperty {
	
    /**
     * ID (for mapping)
     */
    @Id
    @GeneratedValue
    @Column(name="rowid")
    private Integer id;
	
	/**
	 * The property key
	 */
    @Column(name="name")
	private String propertyKey;
	
	/**
	 * Property label (for popup dialog)
	 */
	private String propertyLabel;
	
	/**
	 * The value defined by the user
	 */
	@Column(name="value")
	private String propertyValue;

	/**
	 * The value IS a boolean ?
	 */
	@Column(name="is_boolean_property")
	private Boolean isBooleanProperty;
	
	/**
	 * The value as boolean (if valid for this property)
	 */
	@Column(name="boolean_value")
	private Boolean booleanValue;
	
	/**
	 * Is hidden property ? Like passwords
	 */
	@Column(name="hidden")
	private boolean hidden;


    /**
     * Converter to DTO
     */
    public SimpleGedPluginPropertyDTO convertToDTO() {
        SimpleGedPluginPropertyDTO dto = new SimpleGedPluginPropertyDTO();
        dto.setBooleanValue(getBooleanValue());
        dto.setHidden(getHidden());
        dto.setIsBooleanProperty(getIsBooleanProperty());
        dto.setPropertyKey(getPropertyKey());
        dto.setPropertyLabel(getPropertyLabel());
        dto.setPropertyValue(getPropertyValue());
        return dto;
    }

	
	public GedPluginProperty() {
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

