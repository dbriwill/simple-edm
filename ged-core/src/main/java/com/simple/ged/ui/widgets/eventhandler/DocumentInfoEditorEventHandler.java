package com.simple.ged.ui.widgets.eventhandler;

import java.lang.ref.WeakReference;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.models.GedDocument;
import com.simple.ged.ui.listeners.DocumentInfoEditorListener;
import com.simple.ged.ui.widgets.DocumentInfoEditor;


public class DocumentInfoEditorEventHandler implements EventHandler<KeyEvent> {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(DocumentInfoEditorEventHandler.class);
	
	
	/**
	 * The object we're listerning to
	 */
	private WeakReference<DocumentInfoEditor> documentInfoEditor;

	/**
	 * Event listener
	 */
	private final EventListenerList listeners = new EventListenerList();

	public DocumentInfoEditorEventHandler(DocumentInfoEditor documentInfoEditor) {
		this.documentInfoEditor = new WeakReference<>(documentInfoEditor);
	}
	
	
	/**
	 * Get the document, according to the entered informations
	 */
	public GedDocument getDocument() {
		GedDocument document = new GedDocument();
		document.setName(documentInfoEditor.get().getEditDocumentTitle().getText());
		document.setDate(documentInfoEditor.get().getEditDocumentDate().getSelectedDate());
		document.setDescription(documentInfoEditor.get().getEditDocumentDescription().getText());
		
		return document;
	}
	
	/**
	 * Set the document information, but too stupid to remember the id =)
	 */
	public void setDocument(GedDocument document) {
		
		if (document == null) {
			logger.debug("I get an null document");
			return;
		}
		logger.debug("The document is not null");
		
		documentInfoEditor.get().getEditDocumentTitle().setText(document.getName());
		documentInfoEditor.get().getEditDocumentDate().setSelectedDate(document.getDate());
		documentInfoEditor.get().getEditDocumentDescription().setText(document.getDescription());
	}
	
	
	
	@Override
	public void handle(KeyEvent arg0) {
		boolean valid = isValid();
		for (DocumentInfoEditorListener listener : getDocumentInfoEditorListeners()) {
			listener.selectionChanged(valid);
		}
	}
	
	

	public boolean isValid() {
		boolean valid = true;
		if (documentInfoEditor.get().getEditDocumentTitle().getText().isEmpty()) {
			valid = false;
		}
		return valid;
	}

	
	
	// for externals listeners

	public void addDocumentInfoEditorListener(DocumentInfoEditorListener listener) {
		listeners.add(DocumentInfoEditorListener.class, listener);
	}

	public void removeDocumentInfoEditorListener(DocumentInfoEditorListener listener) {
		listeners.remove(DocumentInfoEditorListener.class, listener);
	}

	public DocumentInfoEditorListener[] getDocumentInfoEditorListeners() {
		return listeners.getListeners(DocumentInfoEditorListener.class);
	}

}
