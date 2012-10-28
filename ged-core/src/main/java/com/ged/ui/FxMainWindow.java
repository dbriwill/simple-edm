package com.ged.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.ui.fxscreen.AboutScreen;
import com.ged.ui.fxscreen.DocumentConfigurationScreen;
import com.ged.ui.fxscreen.FxSoftwareScreen;
import com.ged.ui.fxscreen.FxToolBar;
import com.ged.ui.fxscreen.LibraryViewScreen;
import com.ged.ui.fxscreen.MessageScreen;
import com.ged.ui.fxscreen.PluginConfigurationScreen;
import com.ged.ui.fxscreen.PluginScreen;
import com.tools.FileHelper;
import com.tools.PropertiesHelper;
 

/**
 * The new main window, with javafx power !
 * 
 * @author xavier
 *
 */
public class FxMainWindow extends Application {

	
	private static final Logger logger = Logger.getLogger(FxMainWindow.class);
	
	/**
	 * Default application width
	 */
	public static final int APP_WIDTH = 800;
	
	/**
	 * Default application height
	 */
	public static final int APP_HEIGHT = 600;
	
	
	/**
	 * Proporties loaded from configuration files
	 */
	protected Properties properties;
	
	/**
	 * Loaded screens, you can see this as a stack with the more recent screen at the top (last element)
	 */
	private List<FxSoftwareScreen> screens;
	
	/**
	 * The current application screen, should be the last one of the screen list
	 */
	private FxSoftwareScreen currentCentralScreen = null;
	
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
	private FxToolBar toolBar;
	
	
    @Override
    public void start(Stage primaryStage) {
    	
    	properties = PropertiesHelper.getInstance().getProperties();
    	screens = new ArrayList<>();
    	stage = primaryStage;
    	
        primaryStage.setTitle(properties.getProperty("APPLICATION_NAME"));
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        
        toolBar = new FxToolBar(this); 
        int height = 40;
        toolBar.setPrefHeight(height);
        toolBar.setMinHeight(height);
        toolBar.setMaxHeight(height);
        
        mainLayout = new BorderPane();
        mainLayout.setTop(toolBar);
        

        Scene scene = new Scene(mainLayout, APP_WIDTH, APP_HEIGHT);
        scene.getStylesheets().addAll("templates/style.css", "templates/tools/calendarstyle.css");
        
        primaryStage.setScene(scene);
        
        primaryStage.show();
        
		// default central screen
		setCentralScreen(FxSoftwareScreen.Screen.BROWSING_SCREEN);
    }
    

	/**
	 * Define the central screen, previous central screen won't be lost be lost, we keep screen stack in memory !
	 */
 
	public void setCentralScreen(FxSoftwareScreen.Screen newCentralScreen) {
		
		// When library root isn't valid, always return on settings screen
		if ( ! FileHelper.folderExists(Profile.getInstance().getLibraryRoot())) {
			pushCentralScreen(FxSoftwareScreen.Screen.SETTING_SCREEN);
			return;
		}
		// ---

		pushCentralScreen(newCentralScreen);
	}
	
	
	/**
	 * Push a new central screen on screens stack
	 */
	public void pushCentralScreen(FxSoftwareScreen.Screen screen) {
		
		logger.info("Pushing a central screen");
		
		currentCentralScreen = getScreen(screen);
		screens.add(currentCentralScreen);

		toolBar.fixBackButtonVisibility();;
		
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
			setCentralScreen(FxSoftwareScreen.Screen.BROWSING_SCREEN);
		}
		else {
			currentCentralScreen = screens.get(screens.size() - 1);
			
			mainLayout.setCenter(currentCentralScreen);
		}
	}
	
	
	/**
	 * Return the wanted screen, according to the given value
	 */
	private FxSoftwareScreen getScreen(FxSoftwareScreen.Screen requestedScreen) {
		
		switch (requestedScreen) {
		/*
		case SETTING_SCREEN :
			return new SettingsScreen(this);
		*/
		case ADD_DOC_SCREEN :
		case EDIT_DOC_SCREEN :
			return new DocumentConfigurationScreen(this);
			
		case ABOUT_SCREEN :
			return new AboutScreen(this);
		/*
		case SEARCHING_SCREEN :
			return new SearchingScreen(this);
		*/
		case PLUGIN_CONFIGURATION_SCREEN :
			return new PluginConfigurationScreen(this);
		
		case PLUGIN_MANAGEMENT_SCREEN :
			return new PluginScreen(this);
		
		case MESSAGE_SCREEN :
			return new MessageScreen(this);
		/*	
		case LOCATION_MANAGEMENT_SCREEN :
			return new LocationManagementScreen(this);
		*/
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
		for (FxSoftwareScreen ss : screens) {
			ss.refresh();
		}
	}


	/**
	 * Get the main stage
	 */
	public Stage getMainStage() {
		return stage;
	}


	public FxToolBar getToolBar() {
		return toolBar;
	}
	
}
