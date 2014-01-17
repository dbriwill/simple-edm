package fr.simple.ged.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import fr.simple.ged.common.dto.GedLibraryDto;
import fr.simple.ged.converter.GedLibraryMapper;
import fr.simple.ged.model.GedLibrary;
import fr.simple.ged.repository.GedLibraryRepository;


@Service
@PropertySources(value = {
			@PropertySource("classpath:/library_service.properties")
		}
)
public class GedLibraryService {

    private final Logger logger = LoggerFactory.getLogger(GedLibraryService.class);
    
    @Autowired
    Environment env;

    @Inject
	protected GedLibraryRepository gedLibraryRepository;

    @Inject
	protected GedLibraryMapper gedLibraryConverter;


    public GedLibraryMapper getGedLibraryConverter() {
        return gedLibraryConverter;
    }

    public void setGedLibraryConverter(GedLibraryMapper gedLibraryConverter) {
        this.gedLibraryConverter = gedLibraryConverter;
    }

    public GedLibraryRepository getGedLibraryRepository() {
        return gedLibraryRepository;
    }

    public void setGedLibraryRepository(GedLibraryRepository gedLibraryRepository) {
        this.gedLibraryRepository = gedLibraryRepository;
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
            
            GedLibraryDto libraryDto = new GedLibraryDto();
            libraryDto.setName(env.getProperty("default.library.name"));
            libraryDto.setDescription(env.getProperty("default.library.description"));
            save(libraryDto);
        }
    }
	
	public GedLibrary save(GedLibraryDto gedLibraryDto) {
		return gedLibraryRepository.save(gedLibraryConverter.dtoToModel(gedLibraryDto));
	}
	
}
