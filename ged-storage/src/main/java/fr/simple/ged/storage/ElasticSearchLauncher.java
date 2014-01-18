package fr.simple.ged.storage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;


/**
 * This service must ONLY be used if the embedded storage is active. In other case,
 * elastic search should be directly used
 * 
 * @author xavier
 *
 */
public class ElasticSearchLauncher {

	private static final String MAPPING_DIR = "./mapping";

    /**
     * My logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchLauncher.class);
    
	
    private static Map<String, List<String>> indexesAndTypes;
    
    static {
    	// load elastic search mappings
    	indexesAndTypes = new HashMap<>();
    	
		try {
			// at the top level, we've indexes
			List<String> indexes = IOUtils.readLines(ElasticSearchLauncher.class.getClassLoader().getResourceAsStream(MAPPING_DIR), Charsets.UTF_8);

	    	for (String index : indexes) {
	    		// at the second level, we've types
	    		List<String> types = IOUtils.readLines(ElasticSearchLauncher.class.getClassLoader().getResourceAsStream(MAPPING_DIR + "/" + index), Charsets.UTF_8);
	    		indexesAndTypes.put(index, types);
	    	}
	    	
		} catch (IOException e) {
			logger.error("Failed to read ES mappings !", e);
		}
    }
    
    
    /**
     * Our ES node
     */
    private static Node node;

    
    static {
        // now we can start ES
        node = NodeBuilder.nodeBuilder().node();
        node.client().admin().cluster().prepareHealth().setWaitForYellowStatus().execute().actionGet();

        rebuildEsIndexAndMappingsIfNecessary();
    }
    
    
    public static void closeES() {
    	logger.info("Closing ES");
    	node.close();
    }

    
    /**
     * Action to do on start or on ES restart
     */
    private static void rebuildEsIndexAndMappingsIfNecessary() {

    	logger.info("ES is building indexes and mappings if necessary...");
    	
        // recreate indexes if necessary
    	for (Map.Entry<String, List<String>> entry : indexesAndTypes.entrySet()) {
    		
    		String			index = entry.getKey();
    		List<String>	types = entry.getValue();
    		
	        try {
	        	node.client().admin().indices().prepareCreate(index).execute().actionGet();
	        }
	        catch (IndexAlreadyExistsException e) {
	            logger.info("Index {} already exists", index);
	        }
	        catch (Exception e) {
	            logger.error("Failed to rebuild index {}", index, e);
	        }
	        
	        // load custom mappings
	        for (String type : types) {
		        try {
		        	URL url = Resources.getResource(MAPPING_DIR + "/" + index + "/" + type);
		        	String content = Resources.toString(url, Charsets.UTF_8);
		        	
		        	node.client().admin().indices().preparePutMapping(index).setType(type).setSource(content).execute().actionGet();
		        } catch (IOException e) {
		            logger.error("Failed to read mapping for ES", e);
		        }
	        }
	        
	        node.client().admin().indices().refresh(new RefreshRequest(index)).actionGet();
    	}

        logger.info("Building is over !");
    }

    
    public static String getClusterName() {
    	return node.client().admin().cluster().prepareHealth().get().getClusterName();
    }
    
    
    
    /**
     * Remove all data in ES
     */
    public static void removeAllIndexedData() {
    	
    	for (String index : indexesAndTypes.keySet()) {
            try {
                DeleteIndexResponse delete = node.client().admin().indices().delete(new DeleteIndexRequest(index)).actionGet();
                if (! delete.isAcknowledged()) {
                    logger.error("Index wasn't deleted");
                }
                else {
                    logger.info("Index has been deleted");
                }
            }
            catch (Exception e) {
                logger.error("Failed to delete index, has it been create yet ?");
            }
    	}

        rebuildEsIndexAndMappingsIfNecessary();
    }
}
