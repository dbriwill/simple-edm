package com.simple.ged;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.xmichel.javafx.dialog.Dialog;
import fr.xmichel.toolbox.tools.OSHelper;


/**
 * This class is a singleton for accessing to global program preferences, some kind of profile
 */
public final class Profile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static transient Profile currentProfil = null;

	private static final transient String PROFILE_FILE_NAME = OSHelper.getOSName() + ".profile";
	
	private static final transient Logger logger = LoggerFactory.getLogger(Profile.class);
	
	
	/**
	 * Singleton getter
	 */
	public static synchronized Profile getInstance() {
		if (currentProfil == null) {
			File f = new File(PROFILE_FILE_NAME);
			if ( f.exists() ) {
				currentProfil = loadState();
			} else {
				currentProfil = new Profile();
			}
		}
		return currentProfil;
	}

	/**
	 * Library root
	 */
	private String libraryRoot;

	/**
	 * Current theme name
	 * 
	 * @deprecated Isn't use now. Was in previous versions.
	 */
	@SuppressWarnings("unused")
	private String currentTheme;
	
	/**
	 * Default printer
	 */
	private String defaultPrinter;
	
	
	private Profile() {
		libraryRoot = "Non défini";
		defaultPrinter = "";
	}
	
	
	/**
	 * Define and save library root
	 * 
	 * Note that the final file separator is automatically added
	 * 
	 * Warning : the value isn't save, please call commit to save changes !
	 */
	public synchronized void setDocumentLibraryRoot(String newRoot) {
		libraryRoot  = com.simple.ged.tools.FileHelper.forceUnixSeparator(newRoot);
		libraryRoot += libraryRoot.endsWith("/") ? "" : "/";
	}
	
	/**
	 * Define and save default printer
	 * 
	 * Warning : the value isn't save, please call commit to save changes !
	 */
	public synchronized void setDefaultPrinter(String printerName) {
		defaultPrinter = printerName;
	}
	
	
	/**
	 * Save the changes
	 */
	public synchronized void commitChanges() {
		logger.info("Saving settings");
		saveState();
	}
	
	
	/**
	 * Get the library root
	 * 
	 * Note that the result should contains a final file separator
	 */
	public String getLibraryRoot() {
		String unixFormatRoot = com.simple.ged.tools.FileHelper.forceUnixSeparator(libraryRoot);
		return unixFormatRoot + (unixFormatRoot.endsWith("/") ? "" : "/");
	}

	
	/**
	 * Get the default printer name
	 */
	public String getDefaultPrinterName() {
		return defaultPrinter;
	}
	
	
	/**
	 * Save the profile
	 */
	private synchronized void saveState(){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(PROFILE_FILE_NAME);
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			oos.writeObject(this); 
			oos.flush();
			oos.close();
		} 
		catch (Exception e) {
			logger.error("Error while saving profil", e);
			Dialog.showThrowable("Erreur", "La sauvegarde de votre profil a échouée.", e);
		}
		finally {
			try {
				fos.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * Load the profile
	 * @return
	 * 		The loaded profile
	 */
	private static synchronized Profile loadState(){
		Profile profil = null;
		try {
			FileInputStream fis = new FileInputStream(PROFILE_FILE_NAME);
			ObjectInputStream ois= new ObjectInputStream(fis);
			try {	
				profil = (Profile) ois.readObject(); 
			} 
			finally{
				try{
					ois.close();
				} 
				finally {
					fis.close();
				}
			}
		} 
		catch(Exception e) {
			logger.error("Error while loading profil", e);
			Dialog.showThrowable("Erreur", "Le chargement de votre profil a échouée.", e);
			
			profil = new Profile();
		} 
		return profil;
	}
	
}
