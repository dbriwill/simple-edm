#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.connector.plugins.SimpleGedPluginProperty;
import com.simple.ged.connector.plugins.feedback.SimpleGedPluginException;
import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;


public class SimplePlugin extends SimpleGedGetterPlugin {

	private static final Logger logger = LoggerFactory.getLogger(SimplePlugin.class);
	
	@Override
	public void doGet() throws SimpleGedPluginException {
		logger.info("SimplePlugin plugin launched...");
		
		// do your stuff here !
		
		logger.info("End of SimplePlugin plugin");
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
		fileToCopy.setPropertyValue("/tmp/foo.txt");
		
		// add the property in list
		properties.add(fileToCopy);
		
		// set properties list to our plugin
		p.setProperties(properties);
		
		// define destination file for the try
		p.setDestinationFile("/tmp/toto.txt");
		
		// finally, try our plugin
		try {
			p.doGet();
		} catch (SimpleGedPluginException e) {
			logger.error("Epic fail :", e);
		}
	}
}
