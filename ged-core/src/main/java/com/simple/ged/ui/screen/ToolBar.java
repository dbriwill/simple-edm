package com.simple.ged.ui.screen;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Duration;

import com.simple.ged.ui.MainWindow;
import com.simple.ged.ui.screen.eventhandler.ToolBarEventHandler;

import fr.xmichel.toolbox.tools.PropertiesHelper;

public class ToolBar extends SoftwareScreen {


	/**
	 * All buttons in slide dock are SlideDockButton
	 */
	private class ToolBarButton extends ImageView {
		
		
		/**
		 * Initial and "normal" button opacity
		 */
		private static final double INITIAL_OPACITY = 0.5;
		
		/**
		 * Current transition
		 */
		private FadeTransition currentTransition = null;
		
		/**
		 * Note : toolTipText is not used yet
		 */
		public ToolBarButton(String toolTipText, ToolBarEventHandler eventHandler) {
			super();
			
			setOpacity(INITIAL_OPACITY);
			
			final ToolBarButton self = this;
			
			setOnMouseEntered(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					if (currentTransition != null) {
						currentTransition.stop();
					}
					currentTransition = new FadeTransition(Duration.millis(400), self);
					currentTransition.setFromValue(getOpacity());
					currentTransition.setToValue(1);
					currentTransition.play();
				}
			});
			
			setOnMouseExited(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					if (currentTransition != null) {
						currentTransition.stop();
					}
					currentTransition = new FadeTransition(Duration.millis(200), self);
					currentTransition.setFromValue(getOpacity());
					currentTransition.setToValue(INITIAL_OPACITY);
					currentTransition.play();
				}
			});
			
			setOnMouseClicked(eventHandler);
		}
		
		public void setImage(String imageResource) {
			Image i = new Image(getClass().getResourceAsStream(imageResource));
			setSmooth(true);
			setFitWidth(30);
			setFitHeight(30);
			setImage(i);
		}
	}
	
	/**
	 * About popup button
	 */
	private ToolBarButton btnAbout;
	
	/**
	 * Go to setting screen button
	 */
	private ToolBarButton btnSettings;
	
	/**
	 * Plugin getter management
	 */
	private ToolBarButton btnGetterPluginManagement;

    /**
     * Plugin worker management
     */
    private ToolBarButton btnWorkerPluginManagement;

	/**
	 * Messages
	 */
	private ToolBarButton btnMessages;
	
	/**
	 * Back to previous screen
	 */
	private ToolBarButton btnBack;
	
	/**
	 * Home button
	 */
	private ToolBarButton btnHome;
	
	/**
	 * My event handler
	 */
	private ToolBarEventHandler eventHandler;

	
	/**
	 * The box which contains action buttons
	 */
	private HBox centralBox;
	
	public ToolBar(MainWindow w) {
		super(w);
		
		this.getStyleClass().add("toolbar");
		
		instantiateWidgets();
		
		// fill the bar
		
	    addElement(btnGetterPluginManagement);
	    addElement(btnWorkerPluginManagement);

	    addElement(btnMessages);
	    
	    addElement(btnSettings);
	    addElement(btnAbout);
	    
		HBox leftBox = new HBox(20);
		leftBox.setPadding(new Insets(5, 0, 0, 5));
		
		centralBox.setPadding(new Insets(5, 0, 0, 5));
		
		HBox rightBox = new HBox(20);
		rightBox.setPadding(new Insets(5, 0, 0, 5));
		
		HBox.setHgrow(leftBox, Priority.ALWAYS);
		HBox.setHgrow(rightBox, Priority.ALWAYS);
		
		leftBox.getChildren().addAll(btnBack, btnHome);
		
		this.getChildren().addAll(leftBox, centralBox, rightBox);
		
		fixBackButtonVisibility();
	}
	
	
	/**
	 * Add a button an element the tool bar
	 */
	private void addElement(Node n) {
		centralBox.getChildren().add(n);
	}
	
	
	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		centralBox = new HBox(20); // space between buttons
		
		eventHandler = new ToolBarEventHandler(this);
		
		Properties properties = PropertiesHelper.getInstance().getProperties();
		
		// create buttons
		btnBack  			= new ToolBarButton(properties.getProperty("back"), eventHandler);
		btnSettings			= new ToolBarButton(properties.getProperty("info_settings"), eventHandler);
		btnAbout			= new ToolBarButton(properties.getProperty("info_about") + properties.getProperty("APPLICATION_NAME"), eventHandler);
		btnGetterPluginManagement = new ToolBarButton(properties.getProperty("info_plugin_management"), eventHandler);
		btnMessages			= new ToolBarButton(properties.getProperty("info_messages"), eventHandler);
		btnHome				= new ToolBarButton(properties.getProperty("home"), eventHandler);
        btnWorkerPluginManagement = new ToolBarButton("NOT SPECIFIED", eventHandler);


		// define associated pictures
		Map<ToolBarButton, String> associatedImages = new HashMap<>();
		associatedImages.put(btnBack, 				properties.getProperty("ico_toolbar_back_button"));
		associatedImages.put(btnSettings, 			properties.getProperty("ico_toolbar_pref"));
		associatedImages.put(btnAbout,				properties.getProperty("ico_toolbar_about"));
		associatedImages.put(btnGetterPluginManagement,	properties.getProperty("ico_getter_plugins"));
        associatedImages.put(btnWorkerPluginManagement,	properties.getProperty("ico_worker_plugins"));
        associatedImages.put(btnMessages,			properties.getProperty("ico_toolbar_message_off"));
		associatedImages.put(btnHome,				properties.getProperty("ico_library_root"));
		
		// set pictures
		for (Map.Entry<ToolBarButton, String> e : associatedImages.entrySet()) {
			e.getKey().setImage(e.getValue());
		}
	}

	
	public void fixBackButtonVisibility() {
		btnBack.setVisible(getMainWindow().getScreenStackCount() > 1);
		btnHome.setVisible(getMainWindow().isNotOnHomeScreen() && ! btnBack.isVisible());
	}
	
	public void markNewMessagesAvailable() {
		btnMessages.setImage(properties.getProperty("ico_toolbar_message_on"));
	}
	
	public void markNoNewMessagesAvailable() {
		btnMessages.setImage(properties.getProperty("ico_toolbar_message_off"));
	}
	
	public ToolBarButton getBtnAbout() {
		return btnAbout;
	}


	public ToolBarButton getBtnSettings() {
		return btnSettings;
	}


	public ToolBarButton getBtnGetterPluginManagement() {
		return btnGetterPluginManagement;
	}


	public ToolBarButton getBtnMessages() {
		return btnMessages;
	}


	public ToolBarButton getBtnBack() {
		return btnBack;
	}


	public ToolBarButton getBtnHome() {
		return btnHome;
	}

}
