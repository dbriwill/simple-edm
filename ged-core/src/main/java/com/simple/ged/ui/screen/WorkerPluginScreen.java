package com.simple.ged.ui.screen;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.models.GedGetterPlugin;
import com.simple.ged.ui.MainWindow;
import com.simple.ged.ui.screen.eventhandler.WorkerPluginScreenEventHandler;

/**
 * 
 * The plugin management screen allow you to enable/disable some plugin
 * 
 * @author xavier
 * 
 */
public class WorkerPluginScreen extends SoftwareScreen {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(WorkerPluginScreen.class);

	/**
	 * The table view contains the list of plugins
	 */
	private TableView<GedGetterPlugin> table;

	/**
	 * The plugin list
	 */
	private ObservableList<GedGetterPlugin> pluginsList;

	/**
	 * My event handler
	 */
	private WorkerPluginScreenEventHandler eventHandler;


	public WorkerPluginScreen(MainWindow mw) {
		super(mw);

		instanciateWidgets();

		table.setItems(pluginsList);

		HBox.setHgrow(table, Priority.ALWAYS);

		this.getChildren().addAll(table);
	}

	/**
	 * Create widgets
	 */
	private void instanciateWidgets() {
		eventHandler = new WorkerPluginScreenEventHandler(this);
	}
}
