package fr.simple.ged;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@EnableAutoConfiguration
@ComponentScan(basePackages = {"fr.simple.ged"})
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);


    private static void usage() {
        logger.info("Usage");
        logger.info("-----");
        logger.info("       --disable-embedded-storage | -d");
        logger.info("       --enable-embedded-storage  | -e");
        logger.info("       --help                     | -h");
    }
    

	public static void main(String[] args) {

        // default value
        boolean embeddedStorage = false;

        for (String arg : args) {
            switch (arg) {
                case "--disable-embedded-storage" :
                case "-d" :
                    embeddedStorage = false;
                    break;
                case "--enable-embedded-storage" :
                case "-e" :
                    embeddedStorage = true;
                    break;
                case "--help" :
                case "-h" :
                    usage();
                default :
                    logger.warn("Unknown argument : {}", arg);
            }
        }

        // hello !
        logger.info("==========================================================================");
//        logger.info("Hi, this is {} version {}", constants.get("APPLICATION_NAME"), constants.get("APPLICATION_VERSION"));
//        logger.info("You can report issues on {}", constants.get("APPLICATION_ISSUES_URL"));
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

        logger.info("Embedded storage engine : {}", embeddedStorage);

		if (embeddedStorage) {
			// we're in the full client mode, we have to initialize the storage engine
			//logger.info("ES is started with cluser name {}", ElasticSearchService.getCluserName());
		}
		
        SpringApplication.run(Application.class, args);
    }
}
