package com.simple.ged.update;

import fr.xmichel.toolbox.tools.PropertiesHelper;

import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This file contains address of the online xml files which describes updates
 * 
 * @author xavier
 *
 */
public final class UpdateInformations {

	private static final Logger logger = LoggerFactory.getLogger(UpdateInformations.class);

    /**
     * The list of available channel
     */
    public enum ReleaseChanel {
        STABLE(""),
        DEV("-dev");

        private String suffix;
        ReleaseChanel(String fileSuffix) {
        	suffix = fileSuffix;
        }
        public String getSuffix() {
            return suffix;
        }
    }

	/**
	 * Should not be instantiated
	 */
	private UpdateInformations() {
	}
	
	/**
	 * Path to xml which describe update for GED core
	 */
	public static final String GED_CORE_UPDATE_DESCRIPTOR_PATH;
	
	/**
	 * Path to xml which describe update for updater
	 */
	public static final String UPDATER_UPDATE_DESCRIPTOR_PATH;
	
	/**
	 * The file which contains values
	 */
	public static final String CONSTANT_PROPERTIES_FILE_PATH = "properties/updater_constants.properties";


    /**
     * Properties file
     */
    private static final String PROPERTIES_RESOURCE_FILE = "updater.properties";

    /**
     * Loaded properties
     * {
     *      http.proxy.host
     *      http.proxy.port
     *      release.chanel {
     *                      stable
     *                      dev
     *      }
     * }
     */
    public static final Properties properties;


    public static ReleaseChanel releaseChanel;

	
	static {
		PropertiesHelper.getInstance().load(CONSTANT_PROPERTIES_FILE_PATH);
		GED_CORE_UPDATE_DESCRIPTOR_PATH = PropertiesHelper.getInstance().getProperties().getProperty("GED_CORE_UPDATE_DESCRIPTOR_PATH");
		UPDATER_UPDATE_DESCRIPTOR_PATH  = PropertiesHelper.getInstance().getProperties().getProperty("UPDATER_UPDATE_DESCRIPTOR_PATH");

        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(PROPERTIES_RESOURCE_FILE);
            properties.load(fis);
            fis.close();
        }
        catch (Exception e) {
            logger.warn("Failed to load {}", PROPERTIES_RESOURCE_FILE);
        }

		// proxy
		if (properties.getProperty("http.proxy.host") != null && properties.getProperty("http.proxy.port") != null) {
            logger.info("Proxy configured : {}:{}", properties.getProperty("http.proxy.host"), UpdateInformations.properties.getProperty("http.proxy.port"));
            System.setProperty("http.proxyHost", properties.getProperty("http.proxy.host"));
            System.setProperty("http.proxyPort", properties.getProperty("http.proxy.port"));
        }
		else {
			logger.info("Pas de proxy de configure");
		}
		
		// channel de maj
        releaseChanel = ReleaseChanel.STABLE;

        if (properties.getProperty("release.chanel") != null) { // l'utilisateur a spécifié un chanel de distribution
            if (properties.getProperty("release.chanel").equals("dev")) {
            	logger.info("Release chanel : DEV");
                releaseChanel = ReleaseChanel.DEV;
            }
        }
        else {
        	logger.info("Release chanel : STABLE");
        }
    }
	
}
