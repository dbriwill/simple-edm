package fr.xmichel.ged;

import com.simple.ged.connector.plugins.feedback.SimpleGedPluginException;
import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;
import com.simple.ged.connector.plugins.getter.SimpleGedGetterPluginProperty;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: xavier
 */
public class OrangeMobilePluginTest {

    @Test
    public void testNotThrowException() {

        // Instantiate our plugin
        SimpleGedGetterPlugin p = new OrangeMobilePlugin();

        // create properties list
        List<SimpleGedGetterPluginProperty> properties = new ArrayList<>();

        // create the required properties
        SimpleGedGetterPluginProperty phoneNumber  = new SimpleGedGetterPluginProperty();
        phoneNumber.setPropertyKey("phone_number");
        phoneNumber.setPropertyValue("06XXXXXXXX");

        SimpleGedGetterPluginProperty secretCode  = new SimpleGedGetterPluginProperty();
        secretCode.setPropertyKey("secret_code");
        secretCode.setPropertyValue("XXXXXX");

        // add the property in list
        properties.add(phoneNumber);
        properties.add(secretCode);

        // set properties list to our plugin
        p.setProperties(properties);

        // define destination file for the try
        p.setDestinationFile("orangeMobilePluginTest");

        // finally, try our plugin
        try {
            p.doGet();
        } catch (SimpleGedPluginException e) {
            Assert.assertTrue(false); // should never works... No ?
        }
    }

}
