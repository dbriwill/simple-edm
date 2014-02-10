package fr.simple.ged.service;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import fr.simple.ged.model.GedNode;

@Service
@PropertySources(value = { @PropertySource("classpath:/edm-configuration.properties") })
public class GedNodeService {

    @Inject
    private Environment env;
    
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
	
	public GedNode findOneByPath(String path) {
	    //
	    // not the best way, but may one of the fastest
	    //
	    
	    // find the node name (last non empty item)
	    String[] fragmentedPath = path.split("/");
	    ArrayUtils.reverse(fragmentedPath);
	    int index = 0;
	    while (fragmentedPath[index].isEmpty()) {
	        ++index;
	    }
	    String nodeName = fragmentedPath[index];
	    
	    // candidates have the good name
	    List<GedNode> candidates = ListUtils.union(ListUtils.union(gedLibraryService.findByName(nodeName), gedDirectoryService.findByName(nodeName)), gedDocumentService.findByName(nodeName));

	    // the winner is the one which has the right path
	    for (GedNode node : candidates) {
	        if (getPathOfNode(node).equalsIgnoreCase(path)) {
	            return node;
	        }
	    }
	    
	    return null;
	}

	/**
	 * get the relative path of the given node
	 *  
	 * @param gedNode
	 *             The node you wan't to know the path
	 * @return
	 *             Relative path to this node
	 */
	public String getPathOfNode(GedNode gedNode) {
		String path = gedNode.getName();
		while (gedNode.getParentId() != null) {
			gedNode = findOne(gedNode.getParentId());
			path = gedNode.getName() + "/" + path;
		}
		return path;
	}
	
	/**
     * get the absolute path of the given node
     *  
     * @param gedNode
     *             The node you wan't to know the path
     * @return
     *             Absolute path to this node
     */
	public String getAbsolutePathOfNode(GedNode node) {
	    return env.getProperty("edm.files_path.root") + "/" + getPathOfNode(node);
	}
	
	public List<GedNode> getChildren(String nodeid) {
		return ListUtils.union(gedDocumentService.findByParent(nodeid), gedDirectoryService.findByParent(nodeid));
	}
	
}
