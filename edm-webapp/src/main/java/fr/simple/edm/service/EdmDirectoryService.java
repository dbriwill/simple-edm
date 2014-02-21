package fr.simple.edm.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.simple.edm.model.EdmDirectory;
import fr.simple.edm.model.EdmDocument;
import fr.simple.edm.repository.EdmDirectoryRepository;

@Service
public class EdmDirectoryService {

    private final Logger logger = LoggerFactory.getLogger(EdmDirectoryService.class);

    @Inject
	private EdmDirectoryRepository edmDirectoryRepository;
	
    @Inject
    private EdmNodeService edmNodeService;
    
    public EdmDirectory findOne(String id) {
    	return edmDirectoryRepository.findOne(id);
    }
    
	public EdmDirectory save(EdmDirectory edmDirectory) {
	    edmNodeService.moveNodeIfNecessary(edmDirectory);
		return edmDirectoryRepository.index(edmDirectory);
	}
	
	public List<EdmDirectory> findByParent(String parentId) {
		return edmDirectoryRepository.findByParentId(parentId);
	}

	public List<EdmDirectory> findByName(String name) {
	    return edmDirectoryRepository.findByName(name);
	}
}
