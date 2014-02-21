package fr.simple.edm.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import fr.simple.edm.model.EdmDirectory;
import fr.simple.edm.model.EdmDocument;
import fr.simple.edm.model.EdmLibrary;
import fr.simple.edm.model.EdmNode;

@Service
@PropertySources(value = { @PropertySource("classpath:/edm-configuration.properties") })
public class EdmNodeService {

    private final Logger logger = LoggerFactory.getLogger(EdmNodeService.class);
    
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
	
	
	public List<EdmNode> getChildren(String nodeid) {
		return ListUtils.union(edmDocumentService.findByParent(nodeid), edmDirectoryService.findByParent(nodeid));
	}
	
	public EdmNode save(EdmNode node) {
	    EdmNode edmNode = findOne(node.getId());
	    if (edmNode instanceof EdmDocument) {
	        edmNode = edmDocumentService.save((EdmDocument) node);
	    }
	    else if (edmNode instanceof EdmDirectory) {
	        edmNode = edmDirectoryService.save((EdmDirectory) node);
	    }
	    else if (edmNode instanceof EdmLibrary) {
	        edmNode = edmLibraryService.save((EdmLibrary) node);
	    }
	    return edmNode;
	}
	
    /**
     * get the absolute path of the given node
     *  
     * @param edmNode
     *             The node you wan't to know the path
     * @return
     *             Absolute path to this node
     */
    public String getServerFilePathOfDocument(EdmDocument edmDocument) {
        return env.getProperty("edm.files_path.root") + "/" + getPathOfNode(edmDocument) + "." + edmDocument.getFileExtension();
    }
    
    // other cases
    public String getServerFilePathOfDocument(EdmNode edmNode) {
        return env.getProperty("edm.files_path.root") + "/" + getPathOfNode(edmNode);
    }
	
	/**
	 * Move the node if (it exists) AND (the hierarchy has changed)
	 */
	public void moveNodeIfNecessary(EdmNode node) {
        if (node.getId() != null && ! node.getId().isEmpty()) { // it's an edition, we may wan't to move the previous file location
            
            EdmNode originalDocument    = findOne(node.getId());
            String originalLocation     = getServerFilePathOfDocument(originalDocument);
            String newLocation          = getServerFilePathOfDocument(node);
            
            if (! Paths.get(originalLocation).toFile().exists()) {
                logger.error("Won't move '{}' because it's not exits", originalLocation);
                return;
            }
            
            if (! originalLocation.equals(newLocation)) { // location has changed, move file !
                try {
                    Files.move(Paths.get(originalLocation), Paths.get(newLocation), StandardCopyOption.ATOMIC_MOVE);
                } catch (IOException e) {
                    logger.error("Failed to move file frome '{}' to '{}'", originalDocument, newLocation, e);
                    // TODO notify error
                }
            }
        }
	}
}
