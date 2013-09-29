package com.simple.ged.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;
import com.simple.ged.connector.plugins.worker.SimpleGedWorkerPlugin;
import com.simple.ged.dao.GedPluginRepository;
import com.simple.ged.models.GedGetterPlugin;
import com.simple.ged.models.GedWorkerPlugin;
import com.simple.ged.plugins.PluginManager;
import com.simple.ged.tools.SpringFactory;

/**
 * Plugin service
 * 
 * @author xavier
 *
 */
@Service
public final class GedPluginService {

	private GedPluginRepository gedPluginRepository = SpringFactory.getAppContext().getBean(GedPluginRepository.class);
	
	
	/**
	 * Load and returns all plugins
	 */
	public static List<GedGetterPlugin> getAllGetterPlugins() {
		return PluginManager.getGetterPluginList();
	}
	
	
	/**
	 * Get plugin informations from database
	 * 
	 * @param plugin
	 *            The plugin file name
	 *            
	 * If the plugin is 
	 */
	public GedGetterPlugin getPluginInformations(SimpleGedGetterPlugin plugin) {
		
		GedGetterPlugin pmi = gedPluginRepository.findByFileName(plugin.getJarFileName());
		
		if (pmi == null) {
			pmi = new GedGetterPlugin();
		}

		pmi.setPlugin(plugin);
		
		return pmi;
	}

	
    /**
     * Get plugin informations from nowhere
     *
     * @param plugin
     *            The plugin file name
     *
     * @note : One day may I need to store informations in dabase, that's why this method exists
     */
    public static synchronized GedWorkerPlugin getPluginInformations(SimpleGedWorkerPlugin plugin) {

        GedWorkerPlugin pmi = new GedWorkerPlugin();

        pmi.setPlugin(plugin);

        return pmi;
    }

    
	/**
	 * Add or update the given plugin
	 */
	public void save(GedGetterPlugin pmi) {
		gedPluginRepository.save(pmi);
	}
	
	
	/**
	 * Desactivate and delete saved informations of the given plugin
	 */
	public void desactivatePlugin(GedGetterPlugin pmi) {
		gedPluginRepository.delete(pmi);
	}
}
