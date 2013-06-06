package fr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
}
