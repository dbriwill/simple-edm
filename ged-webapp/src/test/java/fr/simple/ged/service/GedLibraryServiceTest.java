package fr.simple.ged.service;

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

import fr.simple.ged.Application;
import fr.simple.ged.ElasticsearchTestingHelper;
import fr.simple.ged.model.GedLibrary;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { Application.class })
@ComponentScan(basePackages = { "fr.simple.ged" })
@PropertySources(value = {
		@PropertySource("classpath:/properties/default_values.fr_fr.properties")
	}
)
public class GedLibraryServiceTest {

	@Autowired
	private GedLibraryService gedLibraryService;

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
		List<GedLibrary> librairies = gedLibraryService.getGedLibraries();
		
		assertThat(librairies.size()).isEqualTo(1);
		assertThat(librairies.get(0).getName()).isEqualTo(env.getProperty("default.library.name"));
		assertThat(librairies.get(0).getId()).isNotNull();
	}

}
