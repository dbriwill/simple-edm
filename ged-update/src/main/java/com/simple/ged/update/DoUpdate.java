package com.simple.ged.update;

import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * This class is the launcher which perform update
 * 
 * @author xavier
 *
 */
public final class DoUpdate {

	/**
	 * This updater version
	 */
	public static final String UPDATER_VERSION = "1.6";

	
	private static final Logger logger = LoggerFactory.getLogger(DoUpdate.class);
	
	
	/**
	 * Should not be instantiated
	 */
	private DoUpdate() {
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// waiting frame
		
		JFrame frame = new JFrame();
		JLabel dynamicLabel = new JLabel();

		frame.setTitle("Simple GED updater, version " + UPDATER_VERSION);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setSize(550, 275);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel img = new JLabel();
		Image image = null;
		try {
			 URL imgURL = DoUpdate.class.getResource("/images/refresh.png");
			 Toolkit tk = Toolkit.getDefaultToolkit();
			
			 image = tk.getImage(imgURL);
			 img.setIcon(new ImageIcon(image));
			 addLabel(img, frame);
		} catch (Exception e) {
			logger.error("Error while loading image : images/refresh.png", e);
		}

		addLabel(new JLabel("Mise à jour en cours... Veuillez patienter"), frame);
		addLabel(new JLabel("---"), frame);
		addLabel(new JLabel("Simple GED redémarrera automatiquement une"), frame);
		addLabel(new JLabel("fois la mise à jour téléchargée et appliquée"), frame);
		addLabel(new JLabel("---"), frame);
		addLabel(dynamicLabel, frame);
		
		frame.setVisible(true);
		
		// Just a little wait, I wanna be sure the current version of simple GED is closed
		dynamicLabel.setText("Initializing...");
		try {
			Thread.sleep(2000);
		}
		catch(Exception e) {
		}
		
		// now we have to work a little bit
		
		String onlineVersion = UpdateHelper.getVersionNumber(UpdateInformations.GED_CORE_UPDATE_DESCRIPTOR_PATH);
		frame.setTitle("Simple GED - Mise à jour vers la version " + onlineVersion);
		
		logger.info("Simple GED is updating to version " + onlineVersion);
		
		for (Entry<String, String> e : UpdateHelper.getFilesToDownloadMap(UpdateInformations.GED_CORE_UPDATE_DESCRIPTOR_PATH).entrySet()) {
			logger.info(e.getKey() + " => " + e.getValue());
			dynamicLabel.setText("Downloading : " + e.getKey());
			try {
				UpdateHelper.downloadAndReplaceFile(e.getKey(), System.getProperty("user.dir") + File.separator + e.getValue());
			} 
			catch (Exception e2) {
				logger.error("Could not download file", e2);
				dynamicLabel.setText("Error while downloading : " + e.getKey());
				try {
					Thread.sleep(2000);
				}
				catch(Exception e3) {
				}
			}
		}
		
		
		dynamicLabel.setText("Update finished !");
		try {
			Thread.sleep(2000);
		}
		catch(Exception e) {
		}
		
		try {
	        Runtime.getRuntime().exec("java -jar simple_ged.jar");
	        System.exit(0);
		} catch (Exception e) {
			logger.error("Could not re-launch simple ged", e);
			JOptionPane.showMessageDialog(null, "Erreur", "Oups ! Impossible de relancer Simple GED :-/", JOptionPane.ERROR_MESSAGE);
		}
	}

	
	private static void addLabel(JLabel label, Container container) {
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(label);
    }
	
}
