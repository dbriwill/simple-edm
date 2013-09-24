package com.simple.ged.ui.screen.eventhandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.models.GedGetterPlugin;
import com.simple.ged.models.GedPluginProperty;
import com.simple.ged.plugins.PluginManager;
import com.simple.ged.services.PluginService;
import com.simple.ged.ui.screen.GetterPluginConfigurationScreen;

import fr.xmichel.javafx.dialog.Dialog;
import fr.xmichel.toolbox.tools.PropertiesHelper;


/**
 * 
 * This class is the event handler for GetterPluginConfigurationScreen
 * 
 * @author xavier
 *
 */
public class GetterPluginConfigurationScreenEventHandler implements EventHandler<KeyEvent> {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(GetterPluginConfigurationScreenEventHandler.class);
	
	/**
	 * Properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
	/**
	 * The watched screen
	 */
	private WeakReference<GetterPluginConfigurationScreen> pluginConfigurationScreen;
	
	
	public GetterPluginConfigurationScreenEventHandler(GetterPluginConfigurationScreen pluginConfigurationScreen) {
		this.pluginConfigurationScreen = new WeakReference<>(pluginConfigurationScreen);
	}
	
	public void setOnActionEvent(ActionEvent e) {
		
		if (e.getSource() == pluginConfigurationScreen.get().getSave()) {
			
			GedGetterPlugin p = pluginConfigurationScreen.get().getPlugin();
		
			p.setDayOfMonthForUpdate((Integer) pluginConfigurationScreen.get().getComboDayOfMonthForUpdate().getSelectionModel().getSelectedItem());
			p.setDestinationDirectory(pluginConfigurationScreen.get().getLibraryView().getEventHandler().getCurrentItemRelativePath());
			p.setDestinationFilePattern(pluginConfigurationScreen.get().getFieldNamePattern().getText().trim());
			p.setFileName(p.getPlugin().getJarFileName());
			p.setIntervalBetweenUpdates((Integer) pluginConfigurationScreen.get().getComboIntervalBetweenUpdateInMonth().getSelectionModel().getSelectedItem());
			
			List<GedPluginProperty> properties = new ArrayList<>();
			
			for (Entry<GedPluginProperty, Control> entry : pluginConfigurationScreen.get().getPropertiesFieldsMap().entrySet()) {
				if (entry.getValue() instanceof TextField) {
					entry.getKey().setPropertyValue(((TextField)entry.getValue()).getText());
					properties.add(entry.getKey());
				}
				if (entry.getValue() instanceof CheckBox) {
					logger.trace("checkbox is checked : {}", ((CheckBox)entry.getValue()).isSelected());
					entry.getKey().setBooleanValue(((CheckBox)entry.getValue()).isSelected());
					properties.add(entry.getKey());
				}
			}
			p.setPluginProperties(properties);

			PluginService.addOrUpdatePlugin(p);
			PluginManager.launchGetterPluginUpdate(pluginConfigurationScreen.get());

			Dialog.showInfo(GetterPluginConfigurationScreenEventHandler.properties.getProperty("information"), GetterPluginConfigurationScreenEventHandler.properties.getProperty("plugin_is_activated"), pluginConfigurationScreen.get().getMainStage());
			
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
		
		if (pluginConfigurationScreen.get().getComboDayOfMonthForUpdate().getSelectionModel().getSelectedItem() == null) {
			valid = false;
		}

		if (pluginConfigurationScreen.get().getComboIntervalBetweenUpdateInMonth().getSelectionModel().getSelectedItem() == null) {
			valid = false;
		}
		
		for (Entry<GedPluginProperty, Control> e : pluginConfigurationScreen.get().getPropertiesFieldsMap().entrySet()) {
			if (e.getValue() instanceof TextField) {
				if (((TextField) e.getValue()).getText().isEmpty()) {
					valid = false;
					break;
				}
			}
		}
		
		pluginConfigurationScreen.get().getSave().setDisable(!valid);
	}
}
