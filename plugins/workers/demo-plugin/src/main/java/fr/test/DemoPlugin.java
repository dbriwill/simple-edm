package fr.test;

import com.simple.ged.connector.plugins.dto.GedComponentDTO;
import com.simple.ged.connector.plugins.dto.GedFolderDTO;
import com.simple.ged.connector.plugins.dto.GedDocumentDTO;
import com.simple.ged.connector.plugins.feedback.SimpleGedPluginException;
import com.simple.ged.connector.plugins.worker.SimpleGedWorkerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The demo plugin just list file in the ged
 *
 * @author xavier
 *
 */
public class DemoPlugin extends SimpleGedWorkerPlugin {

	private static final Logger logger = LoggerFactory.getLogger(DemoPlugin.class);

	/**
	 * Document count which matches with the given pattern
	 */
	private int matchingDocumentCount = 0;
	
	/**
	 * The given pattern
	 */
	private String pattern = "";
	
	
    @Override
    public String doWork(GedFolderDTO gedRoot) throws SimpleGedPluginException {
        logger.info("Start work");
        
        try {
        	pattern = getPropertyValue("phone_number");
        	recursiveLister(gedRoot);
        }
        catch (Exception e) {
        	throw new SimpleGedPluginException("Quelque chose ne s'est pas bien passé... " + e.getMessage());
        }
        
        logger.info("End of work");
        
		return Integer.toString(matchingDocumentCount) + " document(s) correspondent au filtre défini !";
    }

    private void recursiveLister(GedComponentDTO element) {
        // sur un dossier, on parcours tous ses enfants
        if (element instanceof GedFolderDTO) {
            for (GedComponentDTO componentDTO : ((GedFolderDTO)element).getChildren()) {
                recursiveLister(element);
            }
        }

        // sur un élément, on l'affiche s'il correspond au pattern
        if (element instanceof GedDocumentDTO) {
            GedDocumentDTO doc = (GedDocumentDTO)element;
            
            if (doc.getDocumentName().contains(pattern)) {
                logger.debug("> {}", doc.getFile().getAbsoluteFile());
                ++matchingDocumentCount;
            }
        }
    }
}
