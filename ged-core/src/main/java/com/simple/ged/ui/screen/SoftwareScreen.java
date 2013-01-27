package com.simple.ged.ui.screen;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Properties;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.ui.MainWindow;

import fr.xmichel.toolbox.tools.PropertiesHelper;

/**
 * 
 * Any software screen must inherits from this class
 * 
 * @author xavier
 * 
 */
public abstract class SoftwareScreen extends HBox {

	private static final Logger logger = LoggerFactory.getLogger(SoftwareScreen.class);

	protected Properties properties;

	private WeakReference<MainWindow> mainWindow;

	/**
	 * Application screen list
	 */
	public enum Screen {
		SETTINGS_SCREEN, 
		BROWSING_SCREEN, 
		ADD_DOC_SCREEN, 
		EDIT_DOC_SCREEN, 
		SEARCHING_SCREEN,
        GETTER_PLUGIN_MANAGEMENT_SCREEN,
        GETTER_PLUGIN_CONFIGURATION_SCREEN,
		MESSAGE_SCREEN,
		ABOUT_SCREEN,
		DIRECTORY_EDITION_SCREEN
	}

	public SoftwareScreen(MainWindow mw) {
		mainWindow = new WeakReference<>(mw);
		properties = PropertiesHelper.getInstance().getProperties();
	}

	/**
	 * Show another screen in the central area, the previous central screen is lost
	 * 
	 * @param newCentralScreen
	 *            The new screen to display
	 */
	public void gotoScreen(SoftwareScreen.Screen newCentralScreen) {
		mainWindow.get().setCentralScreen(newCentralScreen);
	}

	/**
	 * Push a new screen on this screen (screen stack)
	 */
	public void pushScreen(SoftwareScreen.Screen screen) {
		mainWindow.get().pushCentralScreen(screen);
	}

	/**
	 * When a screen has finished his work, he's pop and we're back to the previous screen
	 */
	public void finish() {
		mainWindow.get().popScreen();
	}

	/**
	 * Give an extra value to top screen
	 * 
	 * <key, value>
	 */
	public void pushExtraValues(Map<String, Object> extras) {
		mainWindow.get().pushExtraValuesToTopScreen(extras);
	}

	/**
	 * Receive extra values
	 * 
	 * Should be override by child classes
	 */
	public void pullExtraValues(Map<String, Object> extras) {
		logger.warn("Warning : the child screen doesn't take care about extra values");
	}

	/**
	 * Refresh the screen content
	 * 
	 * Should be override by child classes, or not
	 */
	public void refresh() {
	}

	/**
	 * Quit the application
	 */
	public void quit() {
		System.exit(0);
	}

	/**
	 * Refresh all screens in the screen stack
	 */
	public void refreshScreens() {
		mainWindow.get().refreshScreens();
	}

	/**
	 * Notify that's new message available
	 */
	public void notifyNewMessagesAvailable() {
		mainWindow.get().getToolBar().markNewMessagesAvailable();
	}

	/**
	 * Notify that's NO new message available
	 */
	public void notifyNoNewMessagesAvailable() {
		mainWindow.get().getToolBar().markNoNewMessagesAvailable();
	}

	/**
	 * Get the main window stage
	 */
	public Stage getMainStage() {
		return mainWindow.get().getMainStage();
	}
	
	/**
	 * Get the main window
	 */
	public MainWindow getMainWindow() {
		return mainWindow.get();
	}

	/**
	 * Cancel file reading : stop to use resources, close opened ged files/documents !
	 * 
	 * Override me if necessary
	 */
	public void releaseOpenedFiles() {
	}
	
}
