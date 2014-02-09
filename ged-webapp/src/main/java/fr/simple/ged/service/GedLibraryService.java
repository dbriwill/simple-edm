package fr.simple.ged.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import fr.simple.ged.model.GedLibrary;
import fr.simple.ged.model.GedNode;
import fr.simple.ged.repository.GedLibraryRepository;


@Service
@PropertySources(value = {
			@PropertySource("classpath:/properties/default_values.fr_fr.properties")
		}
)
public class GedLibraryService {

    private final Logger logger = LoggerFactory.getLogger(GedLibraryService.class);
    
    @Inject
    private Environment env;

    @Inject
    private GedLibraryRepository gedLibraryRepository;
    
    public GedLibrary findOne(String id) {
    	return gedLibraryRepository.findOne(id);
    }

	public List<GedLibrary> getGedLibraries() {
		List<GedLibrary> gedLibraries = new ArrayList<>();
		for (GedLibrary l : gedLibraryRepository.findAll()) {
			gedLibraries.add(l);
		}
		return gedLibraries;
	}
	
    public void createDefaultLibraryIfNotExists() {
        logger.debug("Checking for at least one library found...");
        if (gedLibraryRepository.count() == 0) {
            logger.info("Creating default library");
            
            GedLibrary library = new GedLibrary();
            library.setName(env.getProperty("default.library.name"));
            library.setDescription(env.getProperty("default.library.description"));
            save(library);
        }
    }
	
	public GedLibrary save(GedLibrary gedLibrary) {
		return gedLibraryRepository.index(gedLibrary);
	}
	
}
