package fr.simple.edm.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fr.simple.edm.model.EdmDirectory;
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
	    Page<EdmDirectory> page = edmDirectoryRepository.findByParentId(parentId, new PageRequest(0, 99, new Sort(Sort.Direction.ASC, "name")));
		return page.getContent();
	}

	public List<EdmDirectory> findByName(String name) {
	    return edmDirectoryRepository.findByName(name);
	}
	
	public void delete(EdmDirectory edmDirectory) {
	    edmDirectoryRepository.delete(edmDirectory);
	}
}
