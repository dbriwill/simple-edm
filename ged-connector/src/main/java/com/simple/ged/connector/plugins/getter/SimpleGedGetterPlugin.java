package com.simple.ged.connector.plugins.getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * This class is the super class of each simple GED getters plugins
 * 
 * A getter is a plugin which action is to retrieve some file from anywhere and add it in the GED. 
 * 
 * @author xavier
 */
public abstract class SimpleGedGetterPlugin {
	
	/**
	 * The jar file name
	 */
	private String jarFileName;
	
	/**
	 * The destination for the downloaded file
	 */
	private String destinationFile;
	
	/**
	 * The plugin name
	 */
	private String pluginName;
	
	/**
	 * The plugin author
	 */
	private String pluginAuthor;
	
	/**
	 * The plugin version
	 */
	private String pluginVersion;
	
	/**
	 * The plugin date
	 */
	private Date pluginDate;
	
	/**
	 * The plugin description
	 */
	private String pluginDescription;
	
	/**
	 * The properties list
	 */
	private List<SimpleGedPluginProperty> properties;
	
	
	
	public SimpleGedGetterPlugin() {
		properties = new ArrayList<>();
	}
	
	
	/**
	 * Get the value of some property
	 */
	public String getPropertyValue(String key) {
		for (SimpleGedPluginProperty p : properties) {
			if (p.getPropertyKey().equals(key)) {
				return p.getPropertyValue();
			}
		}
		return null;
	}
	
	
	/**
	 * This method should get the file which has to be inserted in GED (from a website for example)
	 * 
	 * It should save the file in the destinationDirectory (see getDestinationDirectory())
	 */
	public abstract void doGet() throws SimpleGedPluginException;
	
	
	
	public String getDestinationFile() {
		return destinationFile;
	}
	
	public void setDestinationFile(String destinationFile) {
		this.destinationFile = destinationFile;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public String getPluginAuthor() {
		return pluginAuthor;
	}

	public void setPluginAuthor(String pluginAuthor) {
		this.pluginAuthor = pluginAuthor;
	}

	public String getPluginVersion() {
		return pluginVersion;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}
	
	public Date getPluginDate() {
		return pluginDate;
	}

	public void setPluginDate(Date pluginDate) {
		this.pluginDate = pluginDate;
	}

	public String getPluginDescription() {
		return pluginDescription;
	}

	public void setPluginDescription(String pluginDescription) {
		this.pluginDescription = pluginDescription;
	}

	public List<SimpleGedPluginProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<SimpleGedPluginProperty> properties) {
		this.properties = properties;
	}

	public String getJarFileName() {
		return jarFileName;
	}

	public void setJarFileName(String jarFileName) {
		this.jarFileName = jarFileName;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((jarFileName == null) ? 0 : jarFileName.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SimpleGedGetterPlugin other = (SimpleGedGetterPlugin) obj;
		if (jarFileName == null) {
			if (other.jarFileName != null) {
				return false;
			}
		} else if (!jarFileName.equals(other.jarFileName)) {
			return false;
		}
		return true;
	}
	
}
