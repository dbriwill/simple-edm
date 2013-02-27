package com.simple.ged.connector.plugins.worker;

import java.util.Date;
import java.util.List;

import com.simple.ged.connector.plugins.SimpleGedPluginProperty;
import com.simple.ged.connector.plugins.dto.GedFolderDTO;
import com.simple.ged.connector.plugins.feedback.SimpleGedPluginException;

/**
 * This class is the super class of each simple GED worker plugins
 * 
 * A worker is a plugin which action is to made some action only one time (when the user required it)
 * 
 * @author xavier
 */
public abstract class SimpleGedWorkerPlugin {

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

	/**
	 * Make your works !
	 * 
	 * @param gedRoot
	 * 				The library root as an entry point
	 */
	public abstract void doWork(GedFolderDTO gedRoot) throws SimpleGedPluginException;


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

}
