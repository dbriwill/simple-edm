package fr.simple.edm;

import java.net.URL;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import fr.simple.edm.service.EdmLibraryService;

@EnableAutoConfiguration
@ComponentScan(basePackages = {"fr.simple.edm"})
@PropertySources(value = {
		@PropertySource("classpath:/properties/constants.properties"),
		@PropertySource("classpath:/edm-configuration.properties"),
		@PropertySource("classpath:/application.properties")
	}
)
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);


    private static EdmLibraryService edmLibraryService;

	private static Environment env;
    
	
    @Inject
	public void setEnv(Environment env) {
		Application.env = env;
	}
	
    @Inject
    public void setEdmLibraryService(EdmLibraryService edmLibraryService) {
        Application.edmLibraryService = edmLibraryService;
    }


	public static void main(String[] args) {

	    SpringApplication app = new SpringApplication(Application.class);
        app.setShowBanner(false);
        app.run(args);
        
        // Run this logs AFTER spring bean injection !
        logger.info("==========================================================================");
		logger.info("Hi, this is {} version {}", env.getProperty("APPLICATION_NAME"), env.getProperty("APPLICATION_VERSION"));
		logger.info("You can report issues on {}", env.getProperty("APPLICATION_ISSUES_URL"));
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
        logger.info("[CONFIGURATION] Embedded storage engine : {}", env.getProperty("edm.embedded-storage"));
        
        
        edmLibraryService.createDefaultLibraryIfNotExists();
        
        logger.info("Startup is finished ! Waiting for some user...");
        
        // full desktop mode, open browser
        if ("true".equalsIgnoreCase(env.getProperty("edm.starter.web_browser"))) {
            String location = "http://127.0.0.1:" + env.getProperty("server.port");
            try {
                java.awt.Desktop.getDesktop().browse(new URL(location).toURI());
            }
            catch (Exception e) {
                logger.error("Failed to open browser for url : {}", location, e);
            }
        }
    }
}
