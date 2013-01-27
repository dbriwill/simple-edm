package com.simple.ged.services;

import java.util.List;

import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;
import com.simple.ged.dao.PluginDAO;
import com.simple.ged.models.GedPlugin;
import com.simple.ged.plugins.PluginManager;

/**
 * 
 * Plugin service
 * 
 * @author xavier
 *
 */
public final class PluginService {

	/**
	 * Should not be instantiated
	 */
	private PluginService() {
	}
	
	
	/**
	 * Load and returns all plugins
	 */
	public static List<GedPlugin> getAllPlugins() {
		return PluginManager.getPluginList();
	}
	
	
	
	/**
	 * Get plugin informations from database
	 * 
	 * @param plugin
	 *            The plugin file name
	 *            
	 * If the plugin is 
	 */
	public static synchronized GedPlugin getPluginInformations(SimpleGedGetterPlugin plugin) {
		
		GedPlugin pmi = PluginDAO.getPluginInformations(plugin.getJarFileName());
		
		if (pmi == null) {
			pmi = new GedPlugin();
		}

		pmi.setPlugin(plugin);
		
		return pmi;
	}
	
	
	/**
	 * Add or update the given plugin
	 */
	public static void addOrUpdateDocument(GedPlugin pmi)
	{
		PluginDAO.saveOrUpdate(pmi);
	}
	
	
	/**
	 * Desactivate and delete saved informations of the given plugin
	 */
	public static void desactivatePlugin(GedPlugin pmi)
	{
		PluginDAO.delete(pmi);
	}
}
