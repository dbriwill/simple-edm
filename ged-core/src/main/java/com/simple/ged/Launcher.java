package com.simple.ged;


import javafx.application.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.ui.MainWindow;
import com.simple.ged.update.UpdateInformations;

import fr.xmichel.toolbox.hibernate.sqlite.HibernateUtil;
import fr.xmichel.toolbox.tools.PropertiesHelper;


/**
 * Application launcher
 * @author xavier
 * 
 */
public final class Launcher {

	private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

	
	/**
	 * Should not be instantiated
	 */
	private Launcher() {
	}
	
	
	public static void main(String[] args) {
				
		logger.info("loading properties...");
		
		// load properties
		PropertiesHelper.getInstance().load("properties/strings.properties");
		PropertiesHelper.getInstance().load("properties/icons.properties");
		PropertiesHelper.getInstance().load("properties/constants.properties");
		PropertiesHelper.getInstance().load("properties/update_message.properties");
		PropertiesHelper.getInstance().load(UpdateInformations.CONSTANT_PROPERTIES_FILE_PATH);
		
		// hello !
		logger.info("==========================================================================");
		logger.info("Hi, this is {} version {}", PropertiesHelper.getInstance().getProperties().get("APPLICATION_NAME"), PropertiesHelper.getInstance().getProperties().get("APPLICATION_VERSION"));
		logger.info("You can report issues on {}", PropertiesHelper.getInstance().getProperties().get("APPLICATION_ISSUES_URL"));
		logger.info("Thanks !");
		logger.info("==========================================================================");
		
		// create or update database
		logger.info("open hibernate session...");
		HibernateUtil.getSessionFactory().openSession();
		
		// complete update (messages)
		logger.info("complete update...");
		MiddleProfile.getInstance().completeUpdate();
        
		// The main window
		logger.info("show main window !");
		Application.launch(MainWindow.class, args);
	}

}
