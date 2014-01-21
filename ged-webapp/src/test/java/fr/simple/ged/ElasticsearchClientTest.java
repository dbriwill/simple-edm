package fr.simple.ged;

import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.client.Client;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { Application.class })
@ComponentScan(basePackages = { "fr.simple.ged" })
public class ElasticsearchClientTest {

	
	@Autowired
	private ElasticsearchTestingHelper elasticsearchTestingHelper;
	
	
	/**
	 * Will destroy and rebuild ES_INDEX
	 */
	@Before
	public void setUp() throws Exception {
		elasticsearchTestingHelper.destroyAndRebuildDocumentsIndex();
	}
	
	
	@Test
	public void localNodeShouldBeStartedAndWorking() throws Exception {
		ElasticsearchConfig elasticsearchConfig = new ElasticsearchConfig();
		
		Method getLocalClientMethod = ElasticsearchConfig.class.getDeclaredMethod("localClient");
		getLocalClientMethod.setAccessible(true);
		Client client = (Client) getLocalClientMethod.invoke(elasticsearchConfig);
		
		assertThat(client).isNotNull();
		
		CountResponse response = client.prepareCount(ElasticsearchTestingHelper.ES_INDEX_DOCUMENTS).execute().actionGet();
		assertThat(response.getCount()).isEqualTo(0);
	}
	
}
