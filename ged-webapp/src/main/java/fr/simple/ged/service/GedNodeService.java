package fr.simple.ged.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import fr.simple.ged.model.GedNode;
import fr.simple.ged.repository.GedDirectoryRepository;
import fr.simple.ged.repository.GedDocumentRepository;
import fr.simple.ged.repository.GedLibraryRepository;
import fr.simple.ged.repository.GedNodeRepository;

@Service
public class GedNodeService {

	@Inject
	private GedNodeRepository gedNodeRepository;

	@Inject
	private GedLibraryService gedLibraryService;
	
	public GedNode findOne(String nodeid) {
		return gedLibraryService.findOne(nodeid);
	}

}
