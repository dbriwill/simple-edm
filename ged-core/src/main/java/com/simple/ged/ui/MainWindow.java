package com.simple.ged.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.Profile;
import com.simple.ged.plugins.PluginManager;
import com.simple.ged.ui.screen.AboutScreen;
import com.simple.ged.ui.screen.DirectoryEditionScreen;
import com.simple.ged.ui.screen.DocumentConfigurationScreen;
import com.simple.ged.ui.screen.FakeScreen;
import com.simple.ged.ui.screen.GetterPluginConfigurationScreen;
import com.simple.ged.ui.screen.GetterPluginScreen;
import com.simple.ged.ui.screen.LibraryViewScreen;
import com.simple.ged.ui.screen.MessageScreen;
import com.simple.ged.ui.screen.SettingsScreen;
import com.simple.ged.ui.screen.SoftwareScreen;
import com.simple.ged.ui.screen.ToolBar;
import com.simple.ged.ui.screen.WorkerPluginConfigurationScreen;
import com.simple.ged.ui.screen.WorkerPluginScreen;
import com.simple.ged.update.DoUpdate;
import com.simple.ged.update.UpdateHelper;
import com.simple.ged.update.UpdateInformations;

import fr.xmichel.javafx.dialog.Dialog;
import fr.xmichel.toolbox.tools.FileHelper;
import fr.xmichel.toolbox.tools.PropertiesHelper;
 

/**
 * The new main window, with javafx power !
 * 
 * @author xavier
 *
 */
public class MainWindow extends Application {

	
	private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);
	
	/**
	 * Default application width
	 */
	public static final int APP_WIDTH = 940;
	
	/**
	 * Default application height
	 */
	public static final int APP_HEIGHT = 650;
	
	
	/**
	 * Proporties loaded from configuration files
	 */
	private Properties properties;
	
	/**
	 * Loaded screens, you can see this as a stack with the more recent screen at the top (last element)
	 */
	private List<SoftwareScreen> screens;
	
	/**
	 * The current application screen, should be the last one of the screen list
	 */
	private SoftwareScreen currentCentralScreen = null;
	
	/**
	 * Central screen node
	 */
	private BorderPane mainLayout;
	
	/**
	 * The main stage
	 */
	private Stage stage;
	
	/**
	 * The tool bar
	 */
	private ToolBar toolBar;
	
	
    @Override
    public void start(Stage primaryStage) {
    	
    	properties = PropertiesHelper.getInstance().getProperties();
    	screens = new ArrayList<>();
    	stage = primaryStage;
    	
        primaryStage.setTitle(properties.getProperty("APPLICATION_NAME"));
        
        toolBar = new ToolBar(this); 
        int height = 40;
        toolBar.setPrefHeight(height);
        toolBar.setMinHeight(height);
        toolBar.setMaxHeight(height);
        
        mainLayout = new BorderPane();
        mainLayout.setTop(toolBar);
        

        Scene scene = new Scene(mainLayout, APP_WIDTH, APP_HEIGHT);
        scene.getStylesheets().addAll("templates/style.css", "templates/tools/calendarstyle.css", "templates/tools/fieldset.css");
        
        primaryStage.setScene(scene);
        
        primaryStage.show();
        
		// default central screen
		setCentralScreen(SoftwareScreen.Screen.BROWSING_SCREEN);
		
		// launch plugin update... (threaded)
		PluginManager.launchGetterPluginUpdate(new FakeScreen(this));
		
		// launch upgrade check... (threaded)
		checkForUpgrade();
    }
    

	/**
	 * Define the central screen, previous central screen won't be lost be lost, we keep screen stack in memory !
	 */
 
	public void setCentralScreen(SoftwareScreen.Screen newCentralScreen) {
		
		// When library root isn't valid, always return on settings screen
		if ( ! FileHelper.folderExists(Profile.getInstance().getLibraryRoot())) {
			pushCentralScreen(SoftwareScreen.Screen.SETTINGS_SCREEN);
			return;
		}
		// ---

		screens.clear();
		pushCentralScreen(newCentralScreen);
	}
	
	
	/**
	 * Push a new central screen on screens stack
	 */
	public void pushCentralScreen(SoftwareScreen.Screen screen) {
		
		logger.info("Pushing a central screen");
		
		currentCentralScreen = getScreen(screen);
		screens.add(currentCentralScreen);

		toolBar.fixBackButtonVisibility();
		
		mainLayout.setCenter(currentCentralScreen);
	}
	
	
	/**
	 * Get screen stack count
	 */
	public int getScreenStackCount() {
		return screens.size();
	}
	
	
	/**
	 * Pop the central screen
	 * 
	 * If no screens left, we display the welcome screen
	 */
	public void popScreen() {
		
		screens.remove(currentCentralScreen);

		if (screens.isEmpty()) {
			setCentralScreen(SoftwareScreen.Screen.BROWSING_SCREEN);
		}
		else {
			currentCentralScreen = screens.get(screens.size() - 1);
			
			mainLayout.setCenter(currentCentralScreen);
		}
	}
	
	
	/**
	 * Return the wanted screen, according to the given value
	 */
	private SoftwareScreen getScreen(SoftwareScreen.Screen requestedScreen) {
		
		switch (requestedScreen) {

		case SETTINGS_SCREEN :
			return new SettingsScreen(this);

		case ADD_DOC_SCREEN :
		case EDIT_DOC_SCREEN :
			return new DocumentConfigurationScreen(this);
			
		case ABOUT_SCREEN :
			return new AboutScreen(this);

		case GETTER_PLUGIN_CONFIGURATION_SCREEN :
			return new GetterPluginConfigurationScreen(this);
		
		case GETTER_PLUGIN_MANAGEMENT_SCREEN :
			return new GetterPluginScreen(this);
		
		case WORKER_PLUGIN_CONFIGURATION_SCREEN :
			return new WorkerPluginConfigurationScreen(this);
			
		case WORKER_PLUGIN_MANAGEMENT_SCREEN :
			return new WorkerPluginScreen(this);
			
		case MESSAGE_SCREEN :
			return new MessageScreen(this);
			
		case DIRECTORY_EDITION_SCREEN :
			return new DirectoryEditionScreen(this);
			
		case BROWSING_SCREEN :
		default:
			return new LibraryViewScreen(this);
		}
	}
	
	/**
	 * Give extra values to top screen (current screen)
	 */
	public void pushExtraValuesToTopScreen(Map<String, Object> extra) {
		currentCentralScreen.pullExtraValues(extra);
	}
	
	/**
	 * Refresh screens in stack
	 */
	public void refreshScreens() {
		logger.info("refreshing screens...");
		for (SoftwareScreen ss : screens) {
			ss.refresh();
		}
	}


	/**
	 * Get the main stage
	 */
	public Stage getMainStage() {
		return stage;
	}


	public ToolBar getToolBar() {
		return toolBar;
	}


	public boolean isNotOnHomeScreen() {
		return ! (currentCentralScreen instanceof LibraryViewScreen);
	}
	
	
	
	private void checkForUpgrade() {
		// check for updates
        Platform.runLater(new Runnable() {
			@Override
			public void run() {
				
				/*
				 * Update the updater if necessary
				 */
				
				String onlineUpdaterVersion = UpdateHelper.getVersionNumber(UpdateInformations.UPDATER_UPDATE_DESCRIPTOR_PATH);
				
				if (Float.parseFloat(onlineUpdaterVersion) <= Float.parseFloat(DoUpdate.UPDATER_VERSION)) {
					logger.info("Updater : no new version");
				}
				else {
					logger.info("Updater need to be updated");
					
					for (Entry<String, String> e : UpdateHelper.getFilesToDownloadMap(UpdateInformations.UPDATER_UPDATE_DESCRIPTOR_PATH).entrySet()) {
						logger.debug(e.getKey() + " => " + System.getProperty("user.dir") + File.separator + e.getValue());
						try {
							UpdateHelper.downloadAndReplaceFile(e.getKey(), System.getProperty("user.dir") + File.separator + e.getValue());
						} 
						catch (Exception e2) {
							logger.error("Error while downloading : " + e.getKey(), e2);
						}
					}
				}
				
				
				/*
				 * Now update the core if necessary
				 */
				
				String onlineVersion = UpdateHelper.getVersionNumber(UpdateInformations.GED_CORE_UPDATE_DESCRIPTOR_PATH);
				
				if (Float.parseFloat(onlineVersion) <= Float.parseFloat(PropertiesHelper.getInstance().getProperties().getProperty("APPLICATION_VERSION"))) {
					logger.info("UpdateManager : No new version");
					return;
				}
				
				logger.info("UpdateManager : New version (" + onlineVersion + ") is avaliable");
				
				if (onlineVersion != null) {
					
					Dialog.buildConfirmation("Nouvelle version disponible",
							"Vous utilisez la version " + PropertiesHelper.getInstance().getProperties().getProperty("APPLICATION_VERSION") 
							+ " de Simple GED or la version " + onlineVersion + " est disponible.\n"
							+ "Voulez-vous faire la mise à jour ? (recommandé)"
					)
			        .addYesButton(new EventHandler<ActionEvent>() {
			            @Override
			            public void handle(ActionEvent arg0) {
			            	try {
						        Runtime.getRuntime().exec("java -jar simpleGedUpdateSystem.jar");
						        System.exit(0);
							} catch (Exception e) {
								logger.error("Cannot do upgrade : ", e);
								Dialog.showThrowable("Erreur", "Impossible de lancer l'assistant de mise à jour", e);
							}
			            }
			        })
			        .addNoButton(null)
			        .addCancelButton(null)
			        .build()
			        .show();
				}
			}
		});
	}
	
}
