package com.simple.ged.ui.screen;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.models.GedMessage;
import com.simple.ged.services.MessageService;
import com.simple.ged.ui.MainWindow;

import fr.xmichel.toolbox.tools.DateHelper;
import fr.xmichel.toolbox.tools.FileHelper;


/**
 * This screen show message about plugins comportement
 * 
 * @author xavier
 *
 */
public class MessageScreen extends SoftwareScreen {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(MessageScreen.class);
	
	/**
	 * the about screen is a web view
	 */
	private WebView webView;
	
	/**
	 * The webview html string content
	 */
	private String strMessage;

	
	public MessageScreen(MainWindow mw) {
		super(mw);
		
		instanciateWidget();
		
		String header = "";
		String footer = "";
		Reader reader = new InputStreamReader(AboutScreen.class.getResourceAsStream("/html/message-head.html"));
		try {
			header = FileHelper.readAllStringContent(reader);
			reader.close();
		} catch (IOException e) {
			logger.error("Failed to read /html/message-head.html", e);
		}
		
		Reader reader2 = new InputStreamReader(AboutScreen.class.getResourceAsStream("/html/message-foot.html"));
		try {
			footer = FileHelper.readAllStringContent(reader2);
			reader2.close();
		} catch (IOException e) {
			logger.error("Failed to read /html/message-foot.html", e);
		}

        // strMessage contains all page content
        strMessage = header + strMessage + footer;
		webView.getEngine().loadContent(header + strMessage + footer);

		HBox.setHgrow(webView, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
		
		this.getChildren().add(webView);
		
		MessageService.markAllMessagesAsRead();
		notifyNoNewMessagesAvailable();
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
                        webView.getEngine().loadContent(strMessage);
                    }
                });
            }
        });

		StringBuilder sb = new StringBuilder();
		
		for (GedMessage m : MessageService.getMessages()) {
			
			List<String> classes = new ArrayList<>();
			classes.add("alert");
			
			if (! m.isRead()) {
				classes.add("unread");
			}
			
			if (m.getMessageLevel().equalsIgnoreCase("error")) {
				classes.add("alert-error");
			}
			else if (m.getMessageLevel().equalsIgnoreCase("info")) {
				classes.add("alert-success");
			}
			else {
				classes.add("alert-info");	
			}
			
			String strClass = "";
			for (String s : classes) {
				strClass += s + " ";
			}
			
			sb.append("<div class=\"");
			sb.append(strClass);
			sb.append("\">");
			sb.append(DateHelper.calendarToString(m.getDate()));
			sb.append("<hr/>");
			sb.append(m.getLabel());
			sb.append("</div>");
		}
		
		strMessage = sb.toString();
	}

}
