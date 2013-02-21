package com.simple.ged.ui.screen.eventhandler;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.models.GedGetterPlugin;
import com.simple.ged.ui.screen.SoftwareScreen.Screen;
import com.simple.ged.ui.screen.WorkerPluginScreen;

/**
 * 
 * This class catch and handle plugin screen events
 * 
 * @author xavier
 *
 */
public class WorkerPluginScreenEventHandler {

	/**
	 * Action type
	 *
	 * @author xavier
	 *
	 */
	public enum Action {
		ONE_SHOT_RUN
	}

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(WorkerPluginScreenEventHandler.class);

	/**
	 * The watched screen
	 */
	private WeakReference<WorkerPluginScreen> pluginScreen;


	public WorkerPluginScreenEventHandler(WorkerPluginScreen pluginScreen) {
		this.pluginScreen = new WeakReference<>(pluginScreen);
	}
	
	
	/**
	 * 
	 * Call when the user clicked on some button (activate, desactivate, modify)
	 * 
	 * @param a
	 * 			The action nested by the user
	 * 
	 * @param pmi
	 * 			The concerned plugin
	 */
	public void pluginActionRequired(Action a, final GedGetterPlugin pmi) {
		
		switch (a) {
		
		case ONE_SHOT_RUN :
			
			pluginScreen.get().pushScreen(Screen.WORKER_PLUGIN_CONFIGURATION_SCREEN);
			
			Map<String, Object> extras = new HashMap<>();
			extras.put("ged-plugin", pmi);
			
			pluginScreen.get().pushExtraValues(extras);
			
			break;
			
		default :
			logger.error("Unknown action");
			break;
		}
		
	}

	
}

