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
public class SFRBoxPluginTest {

    @Test
    public void testNotThrowException() {

        // Instantiate our plugin
        SimpleGedGetterPlugin p = new SFRBoxPlugin();

        // create properties list
        List<SimpleGedPluginPropertyDTO> properties = new ArrayList<>();

        // create the required properties
        SimpleGedPluginPropertyDTO phoneNumber  = new SimpleGedPluginPropertyDTO();
        phoneNumber.setPropertyKey("sfr_mail");
        phoneNumber.setPropertyValue("xxxxxx.xxxxxx@sfr.fr");

        SimpleGedPluginPropertyDTO secretCode  = new SimpleGedPluginPropertyDTO();
        secretCode.setPropertyKey("secret_code");
        secretCode.setPropertyValue("XXXXXXXXXX");

        // add the property in list
        properties.add(phoneNumber);
        properties.add(secretCode);

        // set properties list to our plugin
        p.setProperties(properties);

        // define destination file for the try
        p.setDestinationFile("sfrBoxPluginTest");

        // finally, try our plugin
        try {
            p.doGet();
        } catch (SimpleGedPluginException e) {
            Assert.assertTrue(false); // should never works... No ?
        }
    }

}
