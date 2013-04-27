package fr.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.connector.plugins.dto.GedComponentDTO;
import com.simple.ged.connector.plugins.dto.GedDocumentDTO;
import com.simple.ged.connector.plugins.dto.GedFolderDTO;
import com.simple.ged.connector.plugins.feedback.SimpleGedPluginException;
import com.simple.ged.connector.plugins.worker.SimpleGedWorkerPlugin;

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
        	pattern = getPropertyValue("filter");
        	if (pattern == null) {
        		logger.error("Pattern is null !");
        	}
        	else {
        		logger.info("Pattern I'm looking for : {}", pattern);
        	}
        	recursiveLister(gedRoot);
        }
        catch (Exception e) {
        	throw new SimpleGedPluginException("Quelque chose ne s'est pas bien passé : " + e.getMessage(), e);
        }
        
        logger.info("End of work");
        
		return Integer.toString(matchingDocumentCount) + " document(s) correspondent au filtre défini !";
    }

    private void recursiveLister(GedComponentDTO element) {
        // sur un dossier, on parcours tous ses enfants
        if (element instanceof GedFolderDTO) {
            for (GedComponentDTO componentDTO : ((GedFolderDTO)element).getChildren()) {
                recursiveLister(componentDTO);
            }
        }

        // sur un élément, on l'affiche s'il correspond au pattern
        if (element instanceof GedDocumentDTO) {
            GedDocumentDTO doc = (GedDocumentDTO)element;
            
            if (doc.getRelativePathToRoot().contains(pattern)) {
                logger.debug("> {}", doc.getFile().getAbsoluteFile());
                ++matchingDocumentCount;
            }
        }
    }
}
