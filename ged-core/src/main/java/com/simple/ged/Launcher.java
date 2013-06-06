package com.simple.ged;


import javafx.application.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.services.ElasticSearchService;
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
		
		// create or update database
		logger.info("open hibernate session...");
		HibernateUtil.getSessionFactory().openSession();
		
		logger.info("complete update...");
		MiddleProfile.getInstance().completeUpdate();

        // launch document indexation
		logger.info("launch non indexed-docs indexation...");
		new Thread(new Runnable() {
            @Override
            public void run() {
                ElasticSearchService.indexAllNonIndexedDocumentInLibrary();
            }
        }).start();

        
		// The main window
		logger.info("show main window !");
		Application.launch(MainWindow.class, args);
	}

}
