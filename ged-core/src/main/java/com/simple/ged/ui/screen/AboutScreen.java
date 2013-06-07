package com.simple.ged.ui.screen;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.ui.MainWindow;

import fr.xmichel.toolbox.tools.FileHelper;

/**
 * The screen about
 * 
 * @author xavier
 *
 */
public class AboutScreen extends SoftwareScreen {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(AboutScreen.class);
	
	/**
	 * the about screen is a web view
	 */
	private WebView webView;

    /**
     * the webview content (used as cache)
     */
    private String content;

	public AboutScreen(MainWindow mw) {
		super(mw);
		instanciateWidget();

		try {
			Reader reader = new InputStreamReader(AboutScreen.class.getResourceAsStream("/html/about.html"), "UTF-8");
			content = FileHelper.readAllStringContent(reader);
			content = content.replaceAll("\\{\\{ged-version\\}\\}", properties.getProperty("APPLICATION_VERSION"));
			logger.trace("content : " + content);
			webView.getEngine().loadContent(content);
		} catch (IOException e) {
			logger.error("Failed to read file : html/about.html", e);
		}
		
		HBox.setHgrow(webView, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
		
		this.getChildren().add(webView);
	}
	
	
	private void instanciateWidget() {
		webView = new WebView();

        // open links in another browser
        webView.getEngine().locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, final String oldLoc, final String loc) {
                try {
                    Desktop.getDesktop().browse(new URL(loc).toURI());
                }
                catch (Exception e) {
                    logger.error("Failed to open browser for url " + loc, e);
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        webView.getEngine().loadContent(content);
                    }
                });
            }
        });
	}

}
