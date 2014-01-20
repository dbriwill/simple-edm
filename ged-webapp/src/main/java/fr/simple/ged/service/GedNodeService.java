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
		
		GedNode 	gedLibrary		= (GedNode)gedLibraryRepository.findOne(nodeid);
		GedNode		gedDirectory	= (GedNode)gedDirectoryRepository.findOne(nodeid);
		GedNode		gedDocument		= (GedNode)gedDocumentRepository.findOne(nodeid);
		
		if (gedLibrary != null) {
			node = (GedNode)gedLibrary;
		}
		else if (gedDirectory != null) {
			node = (GedNode)gedDirectory;
		}
		else if (gedDocument != null) {
			node = (GedNode)gedDocument;
		}

		return node;
	}

}
