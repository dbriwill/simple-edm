package fr.simple.ged.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import fr.simple.ged.model.GedNode;
import fr.simple.ged.repository.GedDirectoryRepository;
import fr.simple.ged.repository.GedDocumentRepository;
import fr.simple.ged.repository.GedLibraryRepository;

@Service
public class GedNodeService {

	@Inject
	private GedLibraryRepository gedLibraryRepository;
	
	@Inject
	private GedDirectoryRepository gedDirectoryRepository;
	
	@Inject
	private GedDocumentRepository gedDocumentRepository;
	
	
	public GedNode findOne(String nodeid) {
		// we've to find the node type
		GedNode node = null;
		
		node = gedLibraryRepository.findOne(nodeid);
		if (node == null) {
			node = gedDirectoryRepository.findOne(nodeid);
			if (node == null) {
				node = gedDocumentRepository.findOne(nodeid);
			}
		}
		
		return node;
	}

}
