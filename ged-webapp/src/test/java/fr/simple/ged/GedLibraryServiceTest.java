package fr.simple.ged;

import java.lang.reflect.Field;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.node.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.simple.ged.common.dto.GedLibraryDto;
import fr.simple.ged.service.GedLibraryService;

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
	
    /**
     * Node is started and working
     */
    @Test
    public void nodeTest() {
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
    public void getGedLibrairies() {
    	GedLibraryDto gedLibraryDto = new GedLibraryDto();
    	
    	GedLibrary library = gedLibraryService.save(gedLibraryDto);
    	 //gedLibraryService.getGedLibraries();
    }
	
}

