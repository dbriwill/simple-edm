package fr.simple.ged;

import java.lang.reflect.Method;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ElasticsearchTestingHelper {

	
	public static final String ES_INDEX_DOCUMENTS = "documents";
	
	
	@Autowired
	public ElasticsearchConfig elasticsearchConfig;
	
	
	/**
	 * Will destroy and rebuild ES_INDEX
	 */
	public void destroyAndRebuildDocumentsIndex() throws Exception {
//		Method getLocalClientMethod = ElasticsearchConfig.class.getDeclaredMethod("localClient");
//		getLocalClientMethod.setAccessible(true);
//		Client client = (Client) getLocalClientMethod.invoke(elasticsearchConfig);
//		
//		Method rebuildEsMappingMethod = ElasticsearchConfig.class.getDeclaredMethod("buildEsMapping");
//		rebuildEsMappingMethod.setAccessible(true);
//		
//		client.admin().indices().delete(new DeleteIndexRequest(ES_INDEX_DOCUMENTS)).actionGet();
//		rebuildEsMappingMethod.invoke(elasticsearchConfig);
	}
	
}
