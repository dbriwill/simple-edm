package com.simple.ged.services;

import java.util.List;

import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;
import com.simple.ged.dao.PluginDAO;
import com.simple.ged.models.GedGetterPlugin;
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
	public static List<GedGetterPlugin> getAllPlugins() {
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
	public static synchronized GedGetterPlugin getPluginInformations(SimpleGedGetterPlugin plugin) {
		
		GedGetterPlugin pmi = PluginDAO.getPluginInformations(plugin.getJarFileName());
		
		if (pmi == null) {
			pmi = new GedGetterPlugin();
		}

		pmi.setPlugin(plugin);
		
		return pmi;
	}
	
	
	/**
	 * Add or update the given plugin
	 */
	public static void addOrUpdateDocument(GedGetterPlugin pmi)
	{
		PluginDAO.saveOrUpdate(pmi);
	}
	
	
	/**
	 * Desactivate and delete saved informations of the given plugin
	 */
	public static void desactivatePlugin(GedGetterPlugin pmi)
	{
		PluginDAO.delete(pmi);
	}
}
