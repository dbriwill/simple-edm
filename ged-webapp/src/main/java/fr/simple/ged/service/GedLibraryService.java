package fr.simple.ged.service;

import java.util.ArrayList;
import java.util.List;

import fr.simple.ged.common.dto.GedLibraryDto;
import fr.simple.ged.converter.GedLibraryMapper;
import fr.simple.ged.model.GedLibrary;
import fr.simple.ged.repository.GedLibraryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;


@Named
public class GedLibraryService {

    private final Logger logger = LoggerFactory.getLogger(GedLibraryService.class);


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
            // TODO
        }
    }
	
	public GedLibrary save(GedLibraryDto gedLibraryDto) {
		return gedLibraryRepository.save(gedLibraryConverter.dtoToModel(gedLibraryDto));
	}
	
}
