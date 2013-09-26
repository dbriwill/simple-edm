package com.simple.ged.models;

import java.util.ArrayList;
import java.util.List;

import com.simple.ged.connector.plugins.dto.SimpleGedPluginPropertyDTO;
import com.simple.ged.connector.plugins.worker.SimpleGedWorkerPlugin;

/**
 * Some ged worker is a container for SimpleGedPlugin, which add many details about the plugin (for management)
 * 
 * This is different of SimpleGedPlugin because this class has an access to the informations in
 * database.
 * 
 * @author xavier
 *
 */
public class GedWorkerPlugin {

    /**
     * Plugin's file name
     */
    private String fileName;
    
    
    /**
     * Properties attached to this plugin
     */
    private List<GedPluginProperty> pluginProperties;

    public List<SimpleGedPluginPropertyDTO> getPropertiesAsDTO() {
        List<SimpleGedPluginPropertyDTO> properties = new ArrayList<>();
        for (GedPluginProperty p : pluginProperties) {
            properties.add(p.convertToDTO());
        }
        return properties;
    }

    /**
     * The concerned plugin
     */
    private SimpleGedWorkerPlugin plugin;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	public List<GedPluginProperty> getPluginProperties() {
		return pluginProperties;
	}

	public void setPluginProperties(List<GedPluginProperty> pluginProperties) {
		this.pluginProperties = pluginProperties;
	}

	public SimpleGedWorkerPlugin getPlugin() {
		return plugin;
	}

	public void setPlugin(SimpleGedWorkerPlugin plugin) {
		this.plugin = plugin;
	}
}
