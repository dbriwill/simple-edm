package com.simple.ged.ui.screen.eventhandler;

import java.lang.ref.WeakReference;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.ui.screen.ToolBar;
import com.simple.ged.ui.screen.SoftwareScreen.Screen;

/**
 * Tool bar event handler
 * 
 * @author xavier
 *
 */
public class ToolBarEventHandler implements EventHandler<Event> {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ToolBarEventHandler.class);
	
	/**
	 * The controlled object
	 */
	private WeakReference<ToolBar> toolBar;
	
	
	
	public ToolBarEventHandler(ToolBar toolBar) {
		this.toolBar = new WeakReference<>(toolBar);
	}
	
	
	/**
	 * Handle the event
	 */
	@Override
	public void handle(Event action) {
		
		if (action.getSource() == toolBar.get().getBtnBack()) {
			toolBar.get().finish();
			toolBar.get().fixBackButtonVisibility();
		}
		else if (action.getSource() == toolBar.get().getBtnAbout()) {
			toolBar.get().pushScreen(Screen.ABOUT_SCREEN);
		}
		else if (action.getSource() == toolBar.get().getBtnMessages()) {
			toolBar.get().pushScreen(Screen.MESSAGE_SCREEN);
		}
		else if (action.getSource() == toolBar.get().getBtnGetterPluginManagement()) {
			toolBar.get().gotoScreen(Screen.GETTER_PLUGIN_MANAGEMENT_SCREEN);
		}
		else if (action.getSource() == toolBar.get().getBtnWorkerPluginManagement()) {
			toolBar.get().gotoScreen(Screen.WORKER_PLUGIN_MANAGEMENT_SCREEN);
		}
		else if (action.getSource() == toolBar.get().getBtnHome()) {
			toolBar.get().gotoScreen(Screen.BROWSING_SCREEN);
		}
		else if (action.getSource() == toolBar.get().getBtnSettings()) {
			toolBar.get().gotoScreen(Screen.SETTINGS_SCREEN);
		}
		else {
			logger.warn("Not implemented yet, see ToolBarController.handle");
		}
		
	}
	
}
