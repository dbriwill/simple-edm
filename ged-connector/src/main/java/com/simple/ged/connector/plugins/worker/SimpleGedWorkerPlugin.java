package com.simple.ged.connector.plugins.worker;

import java.util.List;

import com.simple.ged.connector.plugins.dto.GedFolderDTO;
import com.simple.ged.connector.plugins.getter.SimpleGedGetterPluginProperty;

/**
 * This class is the super class of each simple GED worker plugins
 * 
 * A worker is a plugin which action is to made some action only one time (when the user required it)
 * 
 * @author xavier
 */
public abstract class SimpleGedWorkerPlugin {

	/**
	 * The properties list
	 */
	private List<SimpleGedGetterPluginProperty> properties;

	/**
	 * Make your works !
	 * 
	 * @param gedRoot
	 * 				The library root as an entry point
	 */
	public abstract void doWork(GedFolderDTO gedRoot);

	
	public List<SimpleGedGetterPluginProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<SimpleGedGetterPluginProperty> properties) {
		this.properties = properties;
	}

}
