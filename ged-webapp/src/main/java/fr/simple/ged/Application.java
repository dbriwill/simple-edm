package fr.simple.ged;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import fr.simple.ged.service.ElasticSearchService;

@ComponentScan
@EnableAutoConfiguration
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	
	public static void main(String[] args) {
		
		if (! Arrays.asList(args).contains("server")) {
			// we're in the full client mode, we have to initialize the storage engine
			logger.info("ES is started with cluser name {}", ElasticSearchService.getCluserName());
		}
		
        SpringApplication.run(Application.class, args);
    }
	
}
