package fr.simple.ged.service;

import javax.inject.Inject;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import fr.simple.ged.model.GedNode;

@Service
public class GedNodeService {

	@Inject
	private GedLibraryService gedLibraryService;
	
	@Inject
	private GedDocumentService gedDocumentService;
	
	@Inject
	private GedDirectoryService gedDirectoryService;
	
	@Inject
	private GedFileService gedFileService;

	public GedNode findOne(String nodeid) {
		return ObjectUtils.firstNonNull(
				gedLibraryService.findOne(nodeid),
				gedDirectoryService.findOne(nodeid),
				gedDocumentService.findOne(nodeid),
				gedFileService.findOne(nodeid)
		);
	}

	public String getPathOfNode(GedNode gedNode) {
		String path = "";
		while (gedNode.getParent() != null) {
			GedNode parent = gedNode.getParent();
			path = parent.getName() + "/" + path;
		}
		return path;
	}
	
}
