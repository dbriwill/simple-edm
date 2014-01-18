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

import fr.simple.ged.common.dto.GedLibraryDto;
import fr.simple.ged.mapper.GedLibraryMapper;
import fr.simple.ged.model.GedLibrary;
import fr.simple.ged.repository.GedLibraryRepository;


@Service
@PropertySources(value = {
			@PropertySource("classpath:/properties/default_values.fr_fr.properties")
		}
)
public class GedLibraryService {

    private final Logger logger = LoggerFactory.getLogger(GedLibraryService.class);
    
    @Inject
    Environment env;

    @Inject
	protected GedLibraryRepository gedLibraryRepository;

    @Inject
	protected GedLibraryMapper gedLibraryMapper;


    public GedLibraryMapper getGedLibraryMapper() {
        return gedLibraryMapper;
    }

    public void setGedLibraryMapper(GedLibraryMapper gedLibraryMapper) {
        this.gedLibraryMapper = gedLibraryMapper;
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
	
	public List<GedLibraryDto> getGedLibrariesDto() {
		List<GedLibraryDto> gedLibrariesDto = new ArrayList<>();
		for (GedLibrary l : gedLibraryRepository.findAll()) {
			gedLibrariesDto.add(gedLibraryMapper.modelToDto(l));
		}
		return gedLibrariesDto;
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
		// TODO [improve me] ; see https://github.com/spring-projects/spring-data-elasticsearch/issues/21 and https://github.com/spring-projects/spring-data-elasticsearch/pull/27
		// unless it's fixed, I set my generated ID
		if (gedLibraryDto.getId() == null || gedLibraryDto.getId().isEmpty()) {
			gedLibraryDto.setId(String.valueOf(System.currentTimeMillis()));
		}
		return gedLibraryRepository.index(gedLibraryMapper.dtoToModel(gedLibraryDto));
	}
	
}
