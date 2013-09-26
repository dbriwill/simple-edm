package com.simple.ged;


import javafx.application.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.simple.ged.services.GedDirectoryService;
import com.simple.ged.ui.MainWindow;
import com.simple.ged.update.UpdateInformations;

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
		logger.info("--------------------------------------------------------------------------");
		logger.info("java.runtime.name          : " + System.getProperty("java.runtime.name"));
		logger.info("java.runtime.version       : " + System.getProperty("java.runtime.version"));
		logger.info("java.specification.name    : " + System.getProperty("java.specification.name"));
		logger.info("java.specification.vendor  : " + System.getProperty("java.specification.vendor"));
		logger.info("java.specification.version : " + System.getProperty("java.specification.version"));
		logger.info("java.vendor                : " + System.getProperty("java.vendor"));
		logger.info("java.version               : " + System.getProperty("java.version"));
		logger.info("java.vm.info               : " + System.getProperty("java.vm.info"));
		logger.info("java.vm.name               : " + System.getProperty("java.vm.name"));
		logger.info("java.vm.version            : " + System.getProperty("java.vm.version"));
		logger.info("os.arch                    : " + System.getProperty("os.arch"));
		logger.info("os.name                    : " + System.getProperty("os.name"));
		logger.info("os.version                 : " + System.getProperty("os.version"));
		logger.info("==========================================================================");
		
		// create or update database
		logger.info("open hibernate session...");
		
		//HibernateUtil.getSessionFactory().openSession();
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext();
		appContext.getEnvironment().setActiveProfiles("ged-jpa");
		String[] locations = { "classpath*:applicationContext.xml" };
		appContext.setConfigLocations(locations);
		appContext.refresh();
		
		GedDirectoryService gedDirectoryService = appContext.getBean(GedDirectoryService.class);
		
		// complete update (messages)
		logger.info("complete update...");
		MiddleProfile.getInstance().completeUpdate();
        
		// The main window
		logger.info("show main window !");
		Application.launch(MainWindow.class, args);
	}

}
