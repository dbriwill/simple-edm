package fr.xmichel.ged;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.simple.ged.connector.plugins.dto.SimpleGedPluginPropertyDTO;
import com.simple.ged.connector.plugins.feedback.SimpleGedPluginException;
import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;

/**
 * User: xavier
 */
public class SFRMobilePluginTest {

    @Test
    public void testNotThrowException() {

        // Instantiate our plugin
        SimpleGedGetterPlugin p = new SFRMobilePlugin();

        // create properties list
        List<SimpleGedPluginPropertyDTO> properties = new ArrayList<>();

        // create the required properties
        SimpleGedPluginPropertyDTO phoneNumber  = new SimpleGedPluginPropertyDTO();
        phoneNumber.setPropertyKey("phone_number");
        phoneNumber.setPropertyValue("06XXXXXXXX");

        SimpleGedPluginPropertyDTO secretCode  = new SimpleGedPluginPropertyDTO();
        secretCode.setPropertyKey("secret_code");
        secretCode.setPropertyValue("XXXXXX");

        // add the property in list
        properties.add(phoneNumber);
        properties.add(secretCode);

        // set properties list to our plugin
        p.setProperties(properties);

        // define destination file for the try
        p.setDestinationFile("sfrMobilePluginTest");

        // finally, try our plugin
        try {
            p.doGet();
        } catch (SimpleGedPluginException e) {
        	// may we don't have internet connection
        	System.out.println("========> " + e.getMessage());
        	if (! e.getMessage().contains("Connection to https://www.sfr.fr refused")) {
        		Assert.assertTrue(false); // should never works... No ?
        	}
        }
    }

}
