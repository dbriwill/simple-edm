package com.ged.ui.fxscreen.eventhandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import org.apache.log4j.Logger;

import com.ged.connector.plugins.SimpleGedPluginProperty;
import com.ged.models.GedPlugin;
import com.ged.plugins.PluginManager;
import com.ged.services.PluginService;
import com.ged.ui.fxscreen.PluginConfigurationScreen;
import com.tools.PropertiesHelper;
import com.tools.javafx.ModalConfirm;
import com.tools.javafx.ModalConfirmResponse;


/**
 * 
 * This class is the event handler for PluginConfigurationScreen
 * 
 * @author xavier
 *
 */
public class PluginConfigurationScreenEventHandler implements EventHandler<KeyEvent> {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(PluginConfigurationScreenEventHandler.class);
	
	/**
	 * Properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
	/**
	 * The watched screen
	 */
	private WeakReference<PluginConfigurationScreen> pluginConfigurationScreen;
	
	
	public PluginConfigurationScreenEventHandler(PluginConfigurationScreen pluginConfigurationScreen) {
		this.pluginConfigurationScreen = new WeakReference<>(pluginConfigurationScreen);
	}
	
	public void setOnActionEvent(ActionEvent e) {
		
		if (e.getSource() == pluginConfigurationScreen.get().getSave()) {
			
			GedPlugin p = pluginConfigurationScreen.get().getPlugin();
		
			p.setDayOfMonthForUpdate((Integer) pluginConfigurationScreen.get().getComboDayOfMonthForUpdate().getSelectionModel().getSelectedItem());
			p.setDestinationDirectory(pluginConfigurationScreen.get().getLibraryView().getEventHandler().getCurrentItemRelativePath());
			p.setDestinationFilePattern(pluginConfigurationScreen.get().getFieldNamePattern().getText().trim());
			p.setFileName(p.getPlugin().getJarFileName());
			p.setIntervalBetweenUpdates((Integer) pluginConfigurationScreen.get().getComboIntervalBetweenUpdateInMonth().getSelectionModel().getSelectedItem());
			
			List<SimpleGedPluginProperty> properties = new ArrayList<SimpleGedPluginProperty>();
			
			for (Entry<SimpleGedPluginProperty, TextField> entry : pluginConfigurationScreen.get().getPropertiesFieldsMap().entrySet()) {
				entry.getKey().setPropertyValue(entry.getValue().getText());
				properties.add(entry.getKey());
			}
			p.setPluginProperties(properties);

			PluginService.addOrUpdateDocument(p);
			PluginManager.launchPluginUpdate(pluginConfigurationScreen.get());

			ModalConfirm.show(pluginConfigurationScreen.get().getMainStage(), new ModalConfirmResponse() {
    			@Override
    			public void confirm() {
    			}
    			@Override
    			public void cancel() {
    			}
    		},  PluginConfigurationScreenEventHandler.properties.getProperty("plugin_is_activated"));

			
			pluginConfigurationScreen.get().refreshScreens();
			pluginConfigurationScreen.get().finish();
			
		}
		else {
			logger.warn("Not implemented yet");
		}
		
	}
	
	@Override
	public void handle(KeyEvent arg0) {
		checkValidity();
	}
	

	/**
	 * Check if currents values are valid, if true, the save button is available
	 */
	public void checkValidity() {
		boolean valid = true;
		
		if ( pluginConfigurationScreen.get().getFieldNamePattern().getText().trim().isEmpty() ) {
			valid = false;
		}

		for (Entry<SimpleGedPluginProperty, TextField> e : pluginConfigurationScreen.get().getPropertiesFieldsMap().entrySet()) {
			if (e.getValue().getText().isEmpty()) {
				valid = false;
				break;
			}
		}
		
		pluginConfigurationScreen.get().getSave().setDisable(!valid);
	}
}