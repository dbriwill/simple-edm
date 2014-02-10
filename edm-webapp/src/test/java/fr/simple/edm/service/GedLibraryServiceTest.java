package fr.simple.edm.service;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import fr.simple.edm.Application;
import fr.simple.edm.ElasticsearchTestingHelper;
import fr.simple.edm.model.EdmLibrary;
import fr.simple.edm.service.EdmLibraryService;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { Application.class })
@ComponentScan(basePackages = { "fr.simple.edm" })
@PropertySources(value = {
		@PropertySource("classpath:/properties/default_values.fr_fr.properties")
	}
)
public class GedLibraryServiceTest {

	@Autowired
	private EdmLibraryService gedLibraryService;

	@Autowired
    private Environment env;
	
	@Autowired
	private ElasticsearchTestingHelper elasticsearchTestingHelper;
	
	
	/**
	 * Will destroy and rebuild ES_INDEX
	 */
	@Before
	public void setUp() throws Exception {
		elasticsearchTestingHelper.destroyAndRebuildIndex(ElasticsearchTestingHelper.ES_INDEX_DOCUMENTS);
		elasticsearchTestingHelper.flushIndex(ElasticsearchTestingHelper.ES_INDEX_DOCUMENTS);
	}
	
	@Test
	public void defaultLibraryIsCreatedAtStart() {
		gedLibraryService.createDefaultLibraryIfNotExists();
		List<EdmLibrary> librairies = gedLibraryService.getEdmLibraries();
		
		assertThat(librairies.size()).isEqualTo(1);
		assertThat(librairies.get(0).getName()).isEqualTo(env.getProperty("default.library.name"));
		assertThat(librairies.get(0).getId()).isNotNull();
	}

}
