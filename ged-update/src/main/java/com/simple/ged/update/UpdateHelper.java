package com.simple.ged.update;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides tools for an easiest update =)
 * 
 * @author xavier
 *
 */
public final class UpdateHelper {

	private static final Logger logger = LoggerFactory.getLogger(UpdateHelper.class);
	
	
	/**
	 * Should not be instantiated
	 */
	private UpdateHelper() {
	}
	
	
	/**
	 * Get the version number in the hosted xml descriptor
	 * 
	 * @param address
	 * 				The address of the online xml
	 * 
	 * @return
	 * 				The version, as the string but should be a float or "0" if the connection failed
	 */
	public static String getVersionNumber(String address) {
		
		String onlineVersion = "0";

        // ajout du prefix a l'url si canal particulier de selectionné
        address += UpdateInformations.releaseChanel.getSuffix();
        logger.info("Getting xml at adress : {}", address);
        
		try {
			URL xmlUrl = new URL(address);
			
			URLConnection urlConnection = xmlUrl.openConnection();
			urlConnection.setUseCaches(false);
			
			urlConnection.connect();
			
			InputStream stream = urlConnection.getInputStream();
						
			SAXBuilder sxb = new SAXBuilder();
			Document xmlDocument = null;
			
			try {
				xmlDocument = sxb.build(stream);
			} catch (Exception e) {
				logger.error("Could not parse xml document " + address, e);
			}
			
			// get root
			Element root = xmlDocument.getRootElement();
			
			// get online version, should be only one loop
			for (Object child : root.getChildren("number")) {
				onlineVersion = ((Element)child).getText();
			}
			
		} catch (Exception e) {
			logger.error("Could not get xml document : " + address, e);
		}

		return onlineVersion;
	}
	
	
	/**
	 * Get the list of files to download which are listed in the online xml
	 * 
	 * 
	 * @return
	 * 			The map of files to download as <url, file>
	 */
	public static Map<String, String> getFilesToDownloadMap(String address) {
		Map<String, String> fileToDownload = new HashMap<String, String>();
		
        // ajout du prefix a l'url si canal particulier de selectionné
        address += UpdateInformations.releaseChanel.getSuffix();
        logger.info("Getting xml at adress : {}", address);
		
		try {
			URL xmlUrl = new URL(address);
			
			URLConnection urlConnection = xmlUrl.openConnection();
			urlConnection.setUseCaches(false);
			
			urlConnection.connect();
			
			InputStream stream = urlConnection.getInputStream();
						
			SAXBuilder sxb = new SAXBuilder();
			Document xmlDocument = null;
			
			try {
				xmlDocument = sxb.build(stream);
			} catch (Exception e) {
				logger.error("Could not parse xml file", e);
			}
			
			// get root
			Element root = xmlDocument.getRootElement();
			
			Element files = root.getChild("files");
			
			// get online version, should be only one loop
			for (Object child : files.getChildren("file")) {
				Element file = (Element)child;
				
				String url  = file.getChildText("url");
				String dest = file.getChildText("destination");
	
				fileToDownload.put(url, dest);
			}
			
		} catch (Exception e) {
			logger.error("Could not get file list", e);
		}
		
		return fileToDownload;
	}
	
	
	/**
	 * Download and replace file
	 * 
	 * @see http://baptiste-wicht.developpez.com/tutoriels/java/update/
	 */
	public static void downloadAndReplaceFile(String onlineFileUrl, String localFilePath) {
	
		// this list contains files which we always replace
		Set<String> alwaysReplaceFiles = new HashSet<>();
		alwaysReplaceFiles.add("simple_ged.jar");
		alwaysReplaceFiles.add("simpleGedUpdateSystem.jar");
		
		// file already exists !
		if (! alwaysReplaceFiles.contains(localFilePath) && new File(localFilePath).exists()) {
			return;
		}
		
		try { 
			// make sur parent exits
			new File(new File(localFilePath).getParent()).mkdirs();
			
			// ... copy it !
			FileUtils.copyURLToFile(new URL(onlineFileUrl), new File(localFilePath));
	      } catch (Exception e) { 
	    	  logger.error("Could not read/write file", e);
	      }
	}
	
}
