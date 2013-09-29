package com.simple.ged.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simple.ged.models.GedGetterPlugin;

/**
 * This class is the DAO for plugins
 * 
 * @author xavier
 *
 */
public interface GedPluginRepository extends JpaRepository<GedGetterPlugin, Integer> {

	/**
	 * Get plugin informations from database
	 * 
	 * @param pluginFileName
	 *            The plugin file name
	 */
	public GedGetterPlugin findByFileName(String fileName);
	
}
