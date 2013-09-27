#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.simple.ged.connector.plugins.dto.SimpleGedPluginPropertyDTO;
import com.simple.ged.connector.plugins.feedback.SimpleGedPluginException;
import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;

import fr.xmichel.ged.SFRBoxPlugin;


public class SimplePluginTest {
	
    @Test
    public void testNotThrowException() {
    	
    	// Instantiate our plugin
        SimpleGedGetterPlugin p = new SimplePlugin();

        // create properties list
        List<SimpleGedPluginPropertyDTO> properties = new ArrayList<>();

        // create the required properties
        SimpleGedPluginPropertyDTO someProperty  = new SimpleGedPluginPropertyDTO();
        phoneNumber.setPropertyKey("identifiant");
        phoneNumber.setPropertyValue("KEEP_IT_SECRET");
        
        SimpleGedPluginPropertyDTO someOtherProperty  = new SimpleGedPluginPropertyDTO();
        secretCode.setPropertyKey("password");
        secretCode.setPropertyValue("KEEP_IT_SECRET");

        
        // add the property in list
        properties.add(someProperty);
        properties.add(someOtherProperty);
        
        // set properties list to our plugin
        p.setProperties(properties);
        
        // define destination file for the try
        p.setDestinationFile("simple_plugin_test");
    	
        // finally, try our plugin
        try {
            p.doGet();
        } catch (SimpleGedPluginException e) {
            Assert.assertTrue(false); // should never works... No ?
        }
    }
	
}
