package fr.test;

import com.simple.ged.connector.plugins.dto.GedComponentDTO;
import com.simple.ged.connector.plugins.dto.GedFolderDTO;
import com.simple.ged.connector.plugins.dto.GedDocumentDTO;
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

    @Override
    public void doWork(GedFolderDTO gedRoot) {
        logger.info("Start work");
        recursiveLister(gedRoot);
        logger.info("End of work");
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
            //if (doc.getDocumentName().contains("")) {
                logger.debug("> {}", doc.getFile().getAbsoluteFile());
            //}
        }
    }
}
