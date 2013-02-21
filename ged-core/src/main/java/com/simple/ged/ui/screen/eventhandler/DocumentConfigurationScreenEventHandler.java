package com.simple.ged.ui.screen.eventhandler;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Properties;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asprise.util.jtwain.Source;
import com.asprise.util.jtwain.SourceManager;
import com.simple.ged.Profile;
import com.simple.ged.models.GedDocument;
import com.simple.ged.models.GedDocumentFile;
import com.simple.ged.services.GedDocumentService;
import com.simple.ged.tools.FileHelper;
import com.simple.ged.ui.listeners.DocumentInfoEditorListener;
import com.simple.ged.ui.listeners.DocumentPreviewListener;
import com.simple.ged.ui.screen.DocumentConfigurationScreen;


import fr.xmichel.javafx.dialog.Dialog;
import fr.xmichel.toolbox.tools.DateTokenGetter;
import fr.xmichel.toolbox.tools.OSHelper;
import fr.xmichel.toolbox.tools.PropertiesHelper;

/**
 * Add or edit document screen controller
 * 
 * @author xavier
 *
 */
public class DocumentConfigurationScreenEventHandler implements DocumentInfoEditorListener, DocumentPreviewListener, EventHandler<ActionEvent> {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(DocumentConfigurationScreenEventHandler.class);

	/**
	 * The watched screen
	 */
	private WeakReference<DocumentConfigurationScreen> addDocumentScreen;

	/**
	 * Properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
	
	public DocumentConfigurationScreenEventHandler(DocumentConfigurationScreen addDocumentScreen) {
		this.addDocumentScreen = new WeakReference<>(addDocumentScreen);
	}

	/**
	 * Check if currents values are valid, if true, the save button is available
	 */
	public void checkValidity() {

		boolean isValid = true;

		if (!addDocumentScreen.get().getDocInfoEditor().getEventHandler().isValid()) {
			isValid = false;
		}

		if (addDocumentScreen.get().getDocumentPreviewer().getEventHandler().isFileListEmpty()) {
			isValid = false;
		}

		addDocumentScreen.get().getBtnSubmit().setDisable(!isValid);
	}

	/**
	 * @see com.simple.ged.ui.listeners.DocumentInfoEditorListener#selectionChanged(boolean)
	 */
	@Override
	public void selectionChanged(boolean isValid) {
		checkValidity();
	}

	/**
	 * @see com.simple.ged.ui.listeners.DocumentPreviewListener#fileCountChanged(int)
	 */
	@Override
	public void fileCountChanged(int newFileCount) {
		checkValidity();
	}

	/**
	 * @see javafx.event.EventHandler#handle(javafx.event.Event)
	 */
	@Override
	public void handle(ActionEvent arg0) {

		if (arg0.getSource() == addDocumentScreen.get().getBtnAddFromFS()) {
			FileChooser fileChooser = new FileChooser();

			// Show open file dialog
			File file = fileChooser.showOpenDialog(null);

			if (file == null) {
				return;
			}
			
			logger.debug("File is " + file.toString());
			
			addDocumentScreen.get().getDocumentPreviewer().addFile(file);
		} else if (arg0.getSource() == addDocumentScreen.get().getBtnAddFromScanner()) {

			if (OSHelper.isWindowsOs()) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Source source = SourceManager.instance().getDefaultSource();
							source.open();
							source.acquireImage();

							final File f = File.createTempFile(DateTokenGetter.getToken(), ".jpg");
							source.saveLastAcquiredImageIntoFile(f);
							Platform.runLater(new Runnable() {
						        @Override
						        public void run() {
						        	addDocumentScreen.get().getDocumentPreviewer().addFile(f);
						        }
						   });
							
						} catch (Exception e) {
							logger.error("Cannot scan image", e);
							Dialog.showThrowable("Erreur", "Impossible de lancer le scanner", e);
						} finally {
							SourceManager.closeSourceManager();
						}
					}
				});
				t.start();
			}
			else { // show cannot be perform message avaible
				Dialog.showError("Erreur", "L'utilisation de la fonction scanner n'est pas (encore) disponible sous votre système d'exploitation");
			}

			
		} else if (arg0.getSource() == addDocumentScreen.get().getBtnSubmit()) {

			addDocumentScreen.get().releaseOpenedFiles();
			
			GedDocument document = addDocumentScreen.get().getDocInfoEditor().getEventHandler().getDocument();
			List<GedDocumentFile> attachedFiles = FileHelper.copyFilesIfNecessary(
					addDocumentScreen.get().getDocumentPreviewer().getEventHandler().getFileList(), 
					new File(Profile.getInstance().getLibraryRoot() + addDocumentScreen.get().getDocumentRelativeDirectory()), 
					addDocumentScreen.get().getDocInfoEditor().getEditDocumentTitle().getText()
				);
			document.setDocumentFiles(attachedFiles);
			
			if (addDocumentScreen.get().getDocument() != null) {
				document.setId(addDocumentScreen.get().getDocument().getId());
			}
			
			GedDocumentService.addOrUpdateDocument(document);

			String message = properties.getProperty("doc_added");
			if (addDocumentScreen.get().getDocument() != null) {
				message = properties.getProperty("doc_modified");
			}
			
			Dialog.showInfo(properties.getProperty("information"), message, addDocumentScreen.get().getMainStage());
			
			// in case of edition, we have finished our job here
			if (addDocumentScreen.get().getDocument() != null) {
				addDocumentScreen.get().finish();
			}
			
			addDocumentScreen.get().refreshScreens();
			
			addDocumentScreen.get().getDocInfoEditor().clearFields();
			addDocumentScreen.get().getDocumentPreviewer().clearPreviewersList();
			
		} else {
			logger.warn("Not implemented yet, see AddDocumentScreenEventHandler.handle");
		}
	}

}
