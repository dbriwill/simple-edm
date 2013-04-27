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

import com.simple.ged.connector.plugins.SimpleGedPluginProperty;
import com.simple.ged.models.GedWorkerPlugin;
import com.simple.ged.plugins.PluginManager;
import com.simple.ged.ui.screen.WorkerPluginConfigurationScreen;

import fr.xmichel.javafx.dialog.Dialog;
import fr.xmichel.toolbox.tools.PropertiesHelper;


/**
 * 
 * This class is the event handler for WorkerPluginConfigurationScreen
 * 
 * @author xavier
 *
 */
public class WorkerPluginConfigurationScreenEventHandler implements EventHandler<KeyEvent> {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(WorkerPluginConfigurationScreenEventHandler.class);

	/**
	 * Properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();

	/**
	 * The watched screen
	 */
	private WeakReference<WorkerPluginConfigurationScreen> pluginConfigurationScreen;


	public WorkerPluginConfigurationScreenEventHandler(WorkerPluginConfigurationScreen pluginConfigurationScreen) {
		this.pluginConfigurationScreen = new WeakReference<>(pluginConfigurationScreen);
	}
	
	public void setOnActionEvent(ActionEvent e) {
		
		if (e.getSource() == pluginConfigurationScreen.get().getSave()) {
			
			GedWorkerPlugin p = pluginConfigurationScreen.get().getPlugin();
		
//			p.setDayOfMonthForUpdate((Integer) pluginConfigurationScreen.get().getComboDayOfMonthForUpdate().getSelectionModel().getSelectedItem());
//			p.setDestinationDirectory(pluginConfigurationScreen.get().getLibraryView().getEventHandler().getCurrentItemRelativePath());
//			p.setDestinationFilePattern(pluginConfigurationScreen.get().getFieldNamePattern().getText().trim());
			p.setFileName(p.getPlugin().getJarFileName());
//			p.setIntervalBetweenUpdates((Integer) pluginConfigurationScreen.get().getComboIntervalBetweenUpdateInMonth().getSelectionModel().getSelectedItem());
			
			List<SimpleGedPluginProperty> properties = new ArrayList<>();
			
			for (Entry<SimpleGedPluginProperty, Control> entry : pluginConfigurationScreen.get().getPropertiesFieldsMap().entrySet()) {
				if (entry.getValue() instanceof TextField) {
					entry.getKey().setPropertyValue(((TextField)entry.getValue()).getText());
					properties.add(entry.getKey());
				}
				if (entry.getValue() instanceof CheckBox) {
					entry.getKey().setBooleanValue(((CheckBox)entry).isSelected());
					properties.add(entry.getKey());
				}
			}
			
			p.setPluginProperties(properties);

//			PluginService.addOrUpdatePlugin(p);
//			PluginManager.launchGetterPluginUpdate(pluginConfigurationScreen.get());

			PluginManager.launchWorkerPlugin(p, pluginConfigurationScreen.get());
			
			Dialog.showInfo(WorkerPluginConfigurationScreenEventHandler.properties.getProperty("information"), WorkerPluginConfigurationScreenEventHandler.properties.getProperty("plugin_is_launched"), pluginConfigurationScreen.get().getMainStage());
			
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
		
		for (Entry<SimpleGedPluginProperty, Control> e : pluginConfigurationScreen.get().getPropertiesFieldsMap().entrySet()) {
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
