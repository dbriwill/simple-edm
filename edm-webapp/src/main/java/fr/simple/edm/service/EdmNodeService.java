package fr.simple.edm.service;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import fr.simple.edm.model.EdmNode;

@Service
@PropertySources(value = { @PropertySource("classpath:/edm-configuration.properties") })
public class EdmNodeService {

    @Inject
    private Environment env;
    
	@Inject
	private EdmLibraryService edmLibraryService;
	
	@Inject
	private EdmDocumentService edmDocumentService;
	
	@Inject
	private EdmDirectoryService edmDirectoryService;

	
	public EdmNode findOne(String nodeid) {
		return ObjectUtils.firstNonNull(
				edmLibraryService.findOne(nodeid),
				edmDirectoryService.findOne(nodeid),
				edmDocumentService.findOne(nodeid)
		);
	}
	
	public EdmNode findOneByPath(String path) {
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
	    List<EdmNode> candidates = ListUtils.union(ListUtils.union(edmLibraryService.findByName(nodeName), edmDirectoryService.findByName(nodeName)), edmDocumentService.findByName(nodeName));

	    // the winner is the one which has the right path
	    for (EdmNode node : candidates) {
	        if (getPathOfNode(node).equalsIgnoreCase(path)) {
	            return node;
	        }
	    }
	    
	    return null;
	}

	/**
	 * get the relative path of the given node
	 *  
	 * @param edmNode
	 *             The node you wan't to know the path
	 * @return
	 *             Relative path to this node
	 */
	public String getPathOfNode(EdmNode edmNode) {
		String path = edmNode.getName();
		while (edmNode.getParentId() != null) {
			edmNode = findOne(edmNode.getParentId());
			path = edmNode.getName() + "/" + path;
		}
		return path;
	}
	
	/**
     * get the absolute path of the given node
     *  
     * @param edmNode
     *             The node you wan't to know the path
     * @return
     *             Absolute path to this node
     */
	public String getAbsolutePathOfNode(EdmNode edmNode) {
	    return env.getProperty("edm.files_path.root") + "/" + getPathOfNode(edmNode);
	}
	
	public List<EdmNode> getChildren(String nodeid) {
		return ListUtils.union(edmDocumentService.findByParent(nodeid), edmDirectoryService.findByParent(nodeid));
	}
	
}
