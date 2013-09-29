package fr.test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.simple.ged.connector.plugins.dto.SimpleGedPluginPropertyDTO;
import com.simple.ged.connector.plugins.feedback.SimpleGedPluginException;
import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;

public class DemoPluginTest {

    /**
     * Try the plugin : some dummy file copy
     */
    @Test
    public void demoPluginTest() {
    	
    	// Prepare
    	
    	URL myTestURL = ClassLoader.getSystemResource("foo.txt");
    	File myFile = null;
    	try {
    		myFile = new File(myTestURL.toURI());
    	}
    	catch (Exception e) {
		}
    	File myTestDir = myFile.getParentFile();
    	
    	String sourceFileAbsolutePath      = myFile.getAbsolutePath();
    	String destinationFileAbsolutePath = myTestDir.getAbsolutePath() + File.separator + "bar.txt";
    			
    	// Instantiate our plugin
		SimpleGedGetterPlugin p = new DemoPlugin();
		
		// create properties list 
		List<SimpleGedPluginPropertyDTO> properties = new ArrayList<>();
		
		// create the required properties
		SimpleGedPluginPropertyDTO fileToCopy  = new SimpleGedPluginPropertyDTO();
		fileToCopy.setPropertyKey("file_to_copy");
		fileToCopy.setPropertyValue(sourceFileAbsolutePath);
		
		// add the property in list
		properties.add(fileToCopy);
		
		// set properties list to our plugin
		p.setProperties(properties);
		
		// define destination file for the try
		p.setDestinationFile(destinationFileAbsolutePath);
		
		// finally, try our plugin
		try {
			p.doGet();
		} catch (SimpleGedPluginException e) {
			Assert.assertTrue(false);
		}
    }
	
}
