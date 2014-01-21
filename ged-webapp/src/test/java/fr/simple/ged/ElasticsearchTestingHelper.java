package fr.simple.ged;

import java.lang.reflect.Field;
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
		Field clientField = ElasticsearchConfig.class.getDeclaredField("client");
		clientField.setAccessible(true);

		Client client = (Client) clientField.get(elasticsearchConfig);
		
		Method rebuildEsMappingMethod = ElasticsearchConfig.class.getDeclaredMethod("buildEsMapping");
		rebuildEsMappingMethod.setAccessible(true);
		
		client.admin().indices().delete(new DeleteIndexRequest(ES_INDEX_DOCUMENTS)).actionGet();
		rebuildEsMappingMethod.invoke(elasticsearchConfig);
	}
	
}
