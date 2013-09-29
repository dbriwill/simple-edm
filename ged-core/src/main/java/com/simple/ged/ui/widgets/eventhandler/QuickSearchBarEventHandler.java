package com.simple.ged.ui.widgets.eventhandler;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.ui.listeners.QuickSearchListener;
import com.simple.ged.ui.widgets.QuickSearchBar;

/**
 * This is the event handler for quick search widget
 * 
 * @author xavier
 *
 */
public class QuickSearchBarEventHandler implements EventHandler<KeyEvent> {

	/**
	 * Auto-submit time in ms
	 */
	private static final long AUTO_SUBMIT_TIME_IN_MS = 1000;
	
	/**
	 * The logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(QuickSearchBarEventHandler.class);
	
	/**
	 * The watched widget
	 */
	private WeakReference<QuickSearchBar> quickSearchBar;
	
	/**
	 * Timer for auto submit
	 */
	private Timer timer;
	
	/**
	 * Event listener
	 */
	private EventListenerList listeners = new EventListenerList();
	
	
	public QuickSearchBarEventHandler(QuickSearchBar quickSearchBar) {
		this.quickSearchBar = new WeakReference<>(quickSearchBar);
	}


	@Override
	public void handle(KeyEvent arg0) {
		if (quickSearchBar.get().getSeachPatternInput().getText().isEmpty() || arg0.getCode().equals(KeyCode.ENTER)) {
			search();
		}
		
		// auto submit search
		try {
			timer.cancel();
			timer.purge();
		} catch (Exception e) { /* can be null or already canceled, it's not a blocker functionality ! */ }
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
				      @Override public void run() {
				    	  	search();
				      }
				});
			}
		}, AUTO_SUBMIT_TIME_IN_MS);
		// end of auto submit search
	}
	
	
	/**
	 * Launch search
	 */
	private void search() {
		logger.info("Searching : " + quickSearchBar.get().getSeachPatternInput().getText());
		for (QuickSearchListener listener : getQuickSearchListener()) {
			listener.search(quickSearchBar.get().getSeachPatternInput().getText());
		}
	}
	
	
	// for externals listeners

	public void addQuickSearchListener(QuickSearchListener listener) {
		listeners.add(QuickSearchListener.class, listener);
	}

	public void removeQuickSearchListenert(QuickSearchListener listener) {
		listeners.remove(QuickSearchListener.class, listener);
	}

	public QuickSearchListener[] getQuickSearchListener() {
		return listeners.getListeners(QuickSearchListener.class);
	}
}
