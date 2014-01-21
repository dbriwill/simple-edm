package fr.simple.ged.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.simple.ged.mapper.GedDirectoryMapper;
import fr.simple.ged.model.GedDirectory;
import fr.simple.ged.repository.GedDirectoryRepository;

@Service
public class GedDirectoryService {

    private final Logger logger = LoggerFactory.getLogger(GedDirectoryService.class);

    @Inject
	private GedDirectoryRepository gedDirectoryRepository;

    @Inject
    private GedDirectoryMapper gedDirectoryMapper;
	
    public GedDirectory findOne(String id) {
    	return gedDirectoryRepository.findOne(id);
    }
    
	public GedDirectory save(GedDirectory gedDirectory) {
		// TODO [improve me] ; see https://github.com/spring-projects/spring-data-elasticsearch/issues/21 and https://github.com/spring-projects/spring-data-elasticsearch/pull/27
		// unless it's fixed, I set my generated ID
		if (gedDirectory.getId() == null || gedDirectory.getId().isEmpty()) {
			gedDirectory.setId(String.valueOf(System.currentTimeMillis()));
		}
		return gedDirectoryRepository.index(gedDirectory);
	}

}
