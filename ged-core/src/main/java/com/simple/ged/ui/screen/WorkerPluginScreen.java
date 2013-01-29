package com.simple.ged.ui.screen;

import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;
import com.simple.ged.models.GedGetterPlugin;
import com.simple.ged.plugins.PluginManager;
import com.simple.ged.ui.MainWindow;
import com.simple.ged.ui.screen.eventhandler.GetterPluginScreenEventHandler;
import com.simple.ged.ui.screen.eventhandler.WorkerPluginScreenEventHandler;
import fr.xmichel.toolbox.tools.DateHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
