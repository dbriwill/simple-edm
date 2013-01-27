package com.simple.ged.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.simple.ged.ui.screen.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.Profile;
import com.simple.ged.plugins.PluginManager;
import com.simple.ged.ui.screen.GetterPluginScreen;

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
		PluginManager.launchPluginUpdate(new FakeScreen(this));
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

		case GETTER_PLUGIN_CONFIGURATION_SCREEN:
			return new GetterPluginConfigurationScreen(this);
		
		case GETTER_PLUGIN_MANAGEMENT_SCREEN:
			return new GetterPluginScreen(this);
		
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
	
}
