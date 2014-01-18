package fr.simple.ged;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import fr.simple.ged.service.GedLibraryService;

@EnableAutoConfiguration
@ComponentScan(basePackages = {"fr.simple.ged"})
@PropertySources(value = {
		@PropertySource("classpath:/properties/constants.properties")
	}
)
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);


    private static GedLibraryService gedLibraryService;

    public GedLibraryService getGedLibraryService() {
        return gedLibraryService;
    }

    public static Environment getEnv() {
		return env;
	}


	private static Environment env;
    
	
    @Inject
	public void setEnv(Environment env) {
		Application.env = env;
	}
	
    @Inject
    public void setGedLibraryService(GedLibraryService gedLibraryService) {
        Application.gedLibraryService = gedLibraryService;
    }


	public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
        
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
        logger.info("[CONFIGURATION] Embedded storage engine : {}", env.getProperty("ged.embedded-storage"));
        
        
        gedLibraryService.createDefaultLibraryIfNotExists();
        
        logger.info("Startup is finished ! Waiting for some user...");
    }
}
