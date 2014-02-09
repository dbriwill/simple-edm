package fr.simple.ged.service;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import fr.simple.ged.model.GedDirectory;
import fr.simple.ged.model.GedDocument;
import fr.simple.ged.model.GedNode;

@Service
public class GedNodeService {

	@Inject
	private GedLibraryService gedLibraryService;
	
	@Inject
	private GedDocumentService gedDocumentService;
	
	@Inject
	private GedDirectoryService gedDirectoryService;

	
	public GedNode findOne(String nodeid) {
		return ObjectUtils.firstNonNull(
				gedLibraryService.findOne(nodeid),
				gedDirectoryService.findOne(nodeid),
				gedDocumentService.findOne(nodeid)
		);
	}

	public String getPathOfNode(GedNode gedNode) {
		String path = gedNode.getName();
		while (gedNode.getParentId() != null) {
			gedNode = findOne(gedNode.getParentId());
			path = gedNode.getName() + "/" + path;
		}
		return path;
	}
	
	public List<GedNode> getChildren(String nodeid) {
		return ListUtils.union(gedDocumentService.findByParent(nodeid), gedDirectoryService.findByParent(nodeid));
	}
	
}
