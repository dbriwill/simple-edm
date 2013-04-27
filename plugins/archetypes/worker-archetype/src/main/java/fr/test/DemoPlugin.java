package fr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.connector.plugins.SimpleGedPluginProperty;
import com.simple.ged.connector.plugins.feedback.SimpleGedPluginException;
import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;

/**
 * The demo plugin just copy the file designed by file_to_copy in the selected ged directory
 * 
 * @author xavier
 *
 */
public class DemoPlugin extends SimpleGedGetterPlugin {

	private static final Logger logger = LoggerFactory.getLogger(DemoPlugin.class);
	
	@Override
	public void doGet() throws SimpleGedPluginException {
		logger.info("Demo plugin launched...");
		
		// open the source
		try (FileInputStream sourceFile = new FileInputStream(new File(getPropertyValue("file_to_copy")))) {
			
			// open the destination
			try (FileOutputStream destinationFile = new FileOutputStream(getDestinationFile())) {

				// perform the copy (buffered)
				byte buffer[] = new byte[512 * 1024];
				int readCount;
				
				while ((readCount = sourceFile.read(buffer)) != -1){
					destinationFile.write(buffer, 0, readCount);
				}
				
			}
			catch (IOException e) {
				logger.error("Cannot open destination file", e);
				throw new SimpleGedPluginException("Impossible d'écrire dans le fichier de destination");
			}
			
		} catch (IOException e){
			logger.error("Cannot open source file ", e);
			throw new SimpleGedPluginException("Impossible de lire le fichier source");
		}
		
		logger.info("End of demo plugin");
	}
	
	
	// for testing
	public static void main(String[] arg) {
		
		// Instantiate our plugin
		SimpleGedGetterPlugin p = new DemoPlugin();
		
		// create properties list 
		List<SimpleGedPluginProperty> properties = new ArrayList<>();
		
		// create the required properties
		SimpleGedPluginProperty fileToCopy  = new SimpleGedPluginProperty();
		fileToCopy.setPropertyKey("file_to_copy");
		fileToCopy.setPropertyValue("D:\\foo.txt");
		
		// add the property in list
		properties.add(fileToCopy);
		
		// set properties list to our plugin
		p.setProperties(properties);
		
		// define destination file for the try
		p.setDestinationFile("D:\\toto.txt");
		
		// finally, try our plugin
		try {
			p.doGet();
		} catch (SimpleGedPluginException e) {
			logger.error("Epic fail :", e);
		}
	}
}
