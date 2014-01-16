package fr.simple.ged;

import static org.elasticsearch.common.io.Streams.copyToStringFromClasspath;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This service must ONLY be used if the embedded storage is active. In other case,
 * elastic search should be directly used
 * 
 * @author xavier
 *
 */
public class ElasticSearchLauncher {

	 /**
     * Index name
     */
    private static final String ES_GED_INDEX = "ged";

    /**
     * ES Indexes
     */
    private static final String[] ES_GED_INDEX_TYPES = new String[] {
    																	"document", 
    																	"message"
    																};

    /**
     * My logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchLauncher.class);


    /**
     * Elastic search plugins folder
     */
    private static final String ES_PLUGIN_DIR = "es/plugins/";

    /**
     * Our ES node
     */
    private static Node node;

    
    static {

        // make sur plugin can be loaded before starting ES
    	
    	// inheritance from old plugin management ...
//    	try {
//    		String mapperPluginAttachmentsDir = ES_PLUGIN_DIR + "mapper-attachments-1.7.0/";
//    		(new File("embedded/elasticsearch-mapper-attachments-1.7.0.zip")).delete();
//			FileUtils.deleteDirectory(new File(mapperPluginAttachmentsDir));
//		} catch (IOException e) {
//			logger.warn("Failed to delete the old ES plugins dir, may not exists...");
//		}
    	
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

        // recreate indexes if necessary
        try {
            node.client().admin().indices().prepareCreate(ES_GED_INDEX).execute().actionGet();
        }
        catch (IndexAlreadyExistsException e) {
            logger.info("Index {} already exists", ES_GED_INDEX);
        }
        catch (Exception e) {
            logger.error("Failed to rebuild index {}", ES_GED_INDEX, e);
        }

        // load custom mappings
        for (String indexType : ES_GED_INDEX_TYPES) {
	        try {
	        	String content = copyToStringFromClasspath("/mapping/" + indexType + ".json");
	        	node.client().admin().indices().preparePutMapping(ES_GED_INDEX).setType(indexType).setSource(content).execute().actionGet();
	        } catch (IOException e) {
	            logger.error("Failed to read mapping for ES", e);
	        }
        }

        node.client().admin().indices().refresh(new RefreshRequest(ES_GED_INDEX)).actionGet();
    }

    
    public static String getClusterName() {
    	return node.client().admin().cluster().prepareHealth().get().getClusterName();
    }
}
