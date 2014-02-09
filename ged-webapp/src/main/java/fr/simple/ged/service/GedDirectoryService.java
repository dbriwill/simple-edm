package fr.simple.ged.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.simple.ged.model.GedDirectory;
import fr.simple.ged.repository.GedDirectoryRepository;

@Service
public class GedDirectoryService {

    private final Logger logger = LoggerFactory.getLogger(GedDirectoryService.class);

    @Inject
	private GedDirectoryRepository gedDirectoryRepository;
	
    public GedDirectory findOne(String id) {
    	return gedDirectoryRepository.findOne(id);
    }
    
	public GedDirectory save(GedDirectory gedDirectory) {
		return gedDirectoryRepository.index(gedDirectory);
	}
	
	public List<GedDirectory> findByParent(String parentId) {
		return gedDirectoryRepository.findByParentId(parentId);
	}

	public List<GedDirectory> findByName(String name) {
	    return gedDirectoryRepository.findByName(name);
	}
}
