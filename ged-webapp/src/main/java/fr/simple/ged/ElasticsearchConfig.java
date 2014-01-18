package fr.simple.ged;


import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.google.common.io.Resources;


@Configuration
@EnableElasticsearchRepositories(basePackages = "fr.simple.ged.repository")
@PropertySources(value = {
		@PropertySource("classpath:/elasticsearch_configuration.properties")
	}
)
public class ElasticsearchConfig {

    /**
     * My logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);
    
    
	/**
	 * Mapping files location
	 */
	private static final String MAPPING_DIR = "./mapping";
	
	
	@Inject
    Environment env;


    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
    	// local case
    	if (env.getProperty("ged.embedded-storage").equalsIgnoreCase("true")) {
    		Client client = localClient();
    		
    		// on a local environment, we wan't to be sure the mapping is applied
    		client.admin().cluster().prepareHealth().setWaitForYellowStatus().execute().actionGet();
    		buildEsMapping(client);
    		
    		return new ElasticsearchTemplate(client);
    	}
    	
    	// remote case, don't care about mapping
    	return new ElasticsearchTemplate(remoteClient());
    }

    
    
    private Client localClient() {
    	logger.info("ES is using a local node");
    	return NodeBuilder.nodeBuilder().local(true).node().client();
    }
 
    
    private Client remoteClient() {
    	String remoteNodes = env.getProperty("elasticsearch.cluster-nodes");
    	logger.info("ES is using remote nodes : {}", remoteNodes);
    	
    	TransportClient client = new TransportClient();
    	for (String clusterNode : remoteNodes.split(",")) {
    		String hostAndPort[] = clusterNode.split(":");
    		client.addTransportAddress(new InetSocketTransportAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
    	}
        return client;
    }
    
    
    private void buildEsMapping(Client client) {
    	
    	logger.info("Trying to build ES mapping");
    	
		try {
			// at the top level, we've indexes
			List<String> indexes = IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream(MAPPING_DIR), Charsets.UTF_8);

			for (String index : indexes) {
			
		        try {
		        	client.admin().indices().prepareCreate(index).execute().actionGet();
		        }
		        catch (IndexAlreadyExistsException e) {
		            logger.info("Index {} already exists", index);
		        }
		        catch (Exception e) {
		            logger.error("Failed to rebuild index {}", index, e);
		        }
		        
		        // at the second level, we've types
	    		List<String> types = IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream(MAPPING_DIR + "/" + index), Charsets.UTF_8);
	    		
	    		for (String type : types) {
	    			type = type.replaceFirst(".json", "");
	    			
			        try {
			        	URL url = Resources.getResource(MAPPING_DIR + "/" + index + "/" + type + ".json");
			        	String content = Resources.toString(url, Charsets.UTF_8);
			        	
			        	client.admin().indices().preparePutMapping(index).setType(type).setSource(content).execute().actionGet();
			        } catch (IOException e) {
			            logger.error("Failed to read mapping for ES", e);
			        }
	    		}
	    		
	    		client.admin().indices().refresh(new RefreshRequest(index)).actionGet();
			}
		} catch (IOException e) {
			logger.error("Failed to create ES mappings !", e);
		}

        logger.info("Building is over !");
    }
    
}
