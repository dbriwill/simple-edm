package fr.simple.ged;

import java.lang.reflect.Field;

import org.elasticsearch.node.Node;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import fr.simple.ged.common.dto.GedLibraryDto;
import fr.simple.ged.model.GedLibrary;
import fr.simple.ged.service.GedLibraryService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { Application.class })
@ComponentScan(basePackages = { "fr.simple.ged" })
public class GedLibraryServiceTest {

	@Autowired
	private GedLibraryService gedLibraryService;

	
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

		Assert.assertNotNull(node);
		Assert.assertFalse(node.isClosed());
	}

	@Test
	public void defaultLibraryIsCreatedAtStart() {
		GedLibraryDto gedLibraryDto = new GedLibraryDto();

		Assert.assertNotNull(gedLibraryService);
		GedLibrary library = gedLibraryService.save(gedLibraryDto);
		// gedLibraryService.getGedLibraries();
	}

}
