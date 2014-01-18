package fr.simple.ged;

import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.List;

import org.elasticsearch.node.Node;
import org.junit.After;
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

import fr.simple.ged.model.GedLibrary;
import fr.simple.ged.service.GedLibraryService;
import fr.simple.ged.storage.ElasticSearchLauncher;

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
    Environment env;
	
	@Before
	public void setUp() throws Exception {
		ElasticSearchLauncher.removeAllIndexedData();
	}

	@After
	public void tearDown() {
	}
	

	@Test
	public void nodeShouldBeStartedAndWorking() {
		Field field = null;
		try {
			field = ElasticSearchLauncher.class.getDeclaredField("node");
		} catch (Exception e) {
			e.printStackTrace();
		}

		field.setAccessible(true);

		Node node = null;
		try {
			node = (Node) field.get(new ElasticSearchLauncher());
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertThat(node).isNotNull();
		assertThat(node.isClosed()).isFalse();
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
