package com.simple.ged.ui.screen.eventhandler;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.simple.ged.models.GedGetterPlugin;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.services.PluginService;
import com.simple.ged.ui.screen.PluginScreen;
import com.simple.ged.ui.screen.SoftwareScreen.Screen;

import fr.xmichel.javafx.dialog.Dialog;
import fr.xmichel.toolbox.tools.PropertiesHelper;

/**
 * 
 * This class catch and handle plugin screen events
 * 
 * @author xavier
 *
 */
public class PluginScreenEventHandler {

	/**
	 * Action type
	 * 
	 * @author xavier
	 *
	 */
	public enum Action {
		ACTIVATE,
		DESACTIVATE,
		MODIFY
	}

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(PluginScreenEventHandler.class);
	
	/**
	 * Properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
	/**
	 * The watched screen
	 */
	private WeakReference<PluginScreen> pluginScreen;
	
	
	public PluginScreenEventHandler(PluginScreen pluginScreen) {
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
		
		case ACTIVATE :
		case MODIFY :
			
			pluginScreen.get().pushScreen(Screen.PLUGIN_CONFIGURATION_SCREEN);
			
			Map<String, Object> extras = new HashMap<>();
			extras.put("ged-plugin", pmi);
			
			pluginScreen.get().pushExtraValues(extras);
			
			break;
			
		case DESACTIVATE :
			
    		Dialog.buildConfirmation(properties.getProperty("delete"), properties.getProperty("wanna_unactivate_item_named").replace("{0}", pmi.getPlugin().getPluginName()), pluginScreen.get().getMainStage())
			.addYesButton(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
    				PluginService.desactivatePlugin(pmi);
    				pluginScreen.get().refreshPluginListContent();
				}
			})
			.addNoButton(null)
			.addCancelButton(null)
			.build()
			.show();

			break;
			
		default :
			logger.error("Unknown action");
			break;
		}
		
	}

	
}

