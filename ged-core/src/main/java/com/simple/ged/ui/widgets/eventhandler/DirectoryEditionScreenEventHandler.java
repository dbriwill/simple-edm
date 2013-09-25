package com.simple.ged.ui.widgets.eventhandler;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.simple.ged.models.GedDirectory;
import com.simple.ged.services.GedDirectoryService;
import com.simple.ged.tools.SpringFactory;
import com.simple.ged.ui.screen.DirectoryEditionScreen;

import fr.xmichel.javafx.dialog.Dialog;
import fr.xmichel.toolbox.tools.FileHelper;

/**
 * 
 * This is the event handler for DirectoryEditionScreen
 * 
 * @author xavier
 * 
 */
public class DirectoryEditionScreenEventHandler implements EventHandler<KeyEvent> {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(DirectoryEditionScreenEventHandler.class);

	// TODO : remove static
	private static GedDirectoryService gedDirectoryService;
	
	static {
		gedDirectoryService = SpringFactory.getAppContext().getBean(GedDirectoryService.class);
	}
	
	
	/**
	 * The managed object
	 */
	private WeakReference<DirectoryEditionScreen> directoryEditionScreen;

	public DirectoryEditionScreenEventHandler(DirectoryEditionScreen directoryEditionScreen) {
		this.directoryEditionScreen = new WeakReference<>(directoryEditionScreen);
	}

	/**
	 * Called when the user has type some url
	 */
	@Override
	public void handle(KeyEvent arg0) {
		String userUrl = directoryEditionScreen.get().getEditIconOnlineLocation().getText();

		boolean imageUrlValid = false;

		// filter accepted formats
		String[] acceptedFormats = new String[] { "png", "jpg", "jpeg" };
		for (String format : acceptedFormats) {
			if (userUrl.endsWith(format)) {
				imageUrlValid = true;
			}
		}

		// final check, was it a valid url ?
		if (imageUrlValid) {
			UrlValidator.getInstance().isValid(userUrl);
		}

		directoryEditionScreen.get().getButtonDownloadOnlineIcon().setDisable(!imageUrlValid);
	}

	public void lanchIconDownload() {
		String userUrl       = directoryEditionScreen.get().getEditIconOnlineLocation().getText();
		String directoryName = directoryEditionScreen.get().getDirectoryRelativeLocation();
		
		if (userUrl.startsWith("www")) {
			userUrl = "http://" + userUrl;
		}
		
		String targetLocationWithoutPrefix = directoryName + "." + FileHelper.getExtension(userUrl).toLowerCase();
		String targetLocation = DirectoryEditionScreen.DIRECTORY_ICON_DIRECTORY + targetLocationWithoutPrefix;
		
		logger.debug("Start downloading : {}", userUrl);
		logger.debug("Target location : {}", targetLocation);
		
		if ( ! downloadAndReplaceFile(userUrl, targetLocation)) {
			return;
		}
		
		GedDirectory dir = gedDirectoryService.findDirectoryByDirectoryPath(directoryName);
		if (dir == null) {
			logger.trace("Instanciate new directory");
			dir = new GedDirectory();
		}
		
		dir.setRelativeDirectoryPath(directoryName);
		dir.setIconPath(targetLocationWithoutPrefix);
		
		gedDirectoryService.save(dir);
		
		directoryEditionScreen.get().refreshScreens();
		
		Dialog.showInfo("Remplacement effectué", "L'icône a bien été modifée !");
	}

	
	/**
	 * Download and replace file
	 * 
	 * @see http://baptiste-wicht.developpez.com/tutoriels/java/update/
	 */
	public static boolean downloadAndReplaceFile(String onlineFileUrl, String localFilePath) {
		URLConnection connection = null;
		InputStream is = null;
		FileOutputStream destinationFile = null;

		try {
			URL url = new URL(onlineFileUrl);
			connection = url.openConnection();

			int length = connection.getContentLength();

			if (length == -1) {
				throw new IOException("Fichier vide");
			}

			is = new BufferedInputStream(connection.getInputStream());

			byte[] data = new byte[length];

			int currentBit = 0;
			int deplacement = 0;

			while (deplacement < length) {
				currentBit = is.read(data, deplacement, data.length - deplacement);
				if (currentBit == -1) {
					break;
				}
				deplacement += currentBit;
			}

			if (deplacement != length) {
				Dialog.showError("Erreur lors du téléchargement", "Le fichier n'a pas été lu en entier (seulement " + deplacement + " sur " + length + ")");
				return false;
			}

			destinationFile = new FileOutputStream(localFilePath);

			destinationFile.write(data);

			destinationFile.flush();

		} catch (MalformedURLException e) {
			logger.error("Wrong url : " + onlineFileUrl, e);
			Dialog.showThrowable("Url mal formatée", "L'url semble incorrecte", e);
			return false;
		} catch (IOException e) {
			logger.error("Could not read source file", e);
			Dialog.showThrowable("Récupération impossible", "Impossible de lire le fichier source", e);
			return false;
		} finally {
			try {
				is.close();
				destinationFile.close();
			} catch (IOException e) {
				logger.error("Could not write target file", e);
				Dialog.showThrowable("Ecriture impossible", "Impossible d'écrire le fichier téléchargé", e);
				return false;
			}
		}
		return true;
	}

}
