package fr.simple.ged;

import java.lang.reflect.Field;

import fr.simple.ged.model.GedLibrary;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.node.Node;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.simple.ged.common.dto.GedLibraryDto;
import fr.simple.ged.service.GedLibraryService;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.boot.context.initializer.ConfigFileApplicationContextInitializer;
import org.springframework.boot.context.initializer.LoggingApplicationContextInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;



@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {Application.class})
public class GedLibraryServiceTest {

	@Inject
	private GedLibraryService gedLibraryService;
	
	public GedLibraryService getGedLibraryService() {
		return gedLibraryService;
	}

	public void setGedLibraryService(GedLibraryService gedLibraryService) {
		this.gedLibraryService = gedLibraryService;
	}

	
	
	@Before
    public void setUp() throws Exception {
		ElasticSearchLauncher.removeAllIndexedData();
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
			node = (Node)field.get(new ElasticSearchLauncher());
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
    	 //gedLibraryService.getGedLibraries();
    }
	
}

