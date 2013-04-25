package com.simple.ged.ui.screen;

import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.connector.plugins.worker.SimpleGedWorkerPlugin;
import com.simple.ged.models.GedWorkerPlugin;
import com.simple.ged.plugins.PluginManager;
import com.simple.ged.ui.MainWindow;
import com.simple.ged.ui.screen.eventhandler.WorkerPluginScreenEventHandler;

import fr.xmichel.toolbox.tools.DateHelper;

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
	private TableView<GedWorkerPlugin> table;

	/**
	 * The plugin list
	 */
	private ObservableList<GedWorkerPlugin> pluginsList;

	/**
	 * My event handler
	 */
	private WorkerPluginScreenEventHandler eventHandler;


	public WorkerPluginScreen(MainWindow mw) {
		super(mw);

		instanciateWidgets();
		refreshPluginListContent();
		
		table.setItems(pluginsList);

		HBox.setHgrow(table, Priority.ALWAYS);

		this.getChildren().addAll(table);
	}

	/**
	 * Create widgets
	 */
	private void instanciateWidgets() {
		eventHandler = new WorkerPluginScreenEventHandler(this);
		
		pluginsList = FXCollections.observableArrayList();
		table = new TableView<>();
	
		TableColumn<GedWorkerPlugin, VBox> colName = new TableColumn<>(properties.getProperty("plugin_name"));
		colName.setCellValueFactory(new Callback<CellDataFeatures<GedWorkerPlugin, VBox>, ObservableValue<VBox>>() {
			public ObservableValue<VBox> call(CellDataFeatures<GedWorkerPlugin, VBox> p) {
				VBox box = new VBox();

				SimpleGedWorkerPlugin plugin = p.getValue().getPlugin();
				
				Label title = new Label(plugin.getPluginName());
				title.getStyleClass().add("list-plugin-title");
				
				Label version = new Label(properties.getProperty("Version") + " " + plugin.getPluginVersion() + " " + properties.getProperty("released_the") + " " + DateHelper.calendarToString(plugin.getPluginDate()));
				version.getStyleClass().add("list-plugin-version");
				
				Label author = new Label(properties.getProperty("by") + " " + plugin.getPluginAuthor());
				author.getStyleClass().add("list-plugin-author");
				
				Label jarName = new Label(plugin.getJarFileName());
				jarName.getStyleClass().add("list-plugin-jarname");
				
				box.getChildren().addAll(title, version, author, new Separator(), jarName);

				return new SimpleObjectProperty<>(box);
			}
		});

		TableColumn<GedWorkerPlugin, VBox> colDesc = new TableColumn<>(properties.getProperty("plugin_description"));
		colDesc.setCellValueFactory(new Callback<CellDataFeatures<GedWorkerPlugin, VBox>, ObservableValue<VBox>>() {
			public ObservableValue<VBox> call(CellDataFeatures<GedWorkerPlugin, VBox> p) {
				VBox box = new VBox();

				GedWorkerPlugin pmi = p.getValue();
				SimpleGedWorkerPlugin plugin = pmi.getPlugin();
				
				logger.trace("Contient \\n ? {}", plugin.getPluginDescription().contains("\\n"));
				logger.trace(plugin.getPluginDescription().replace("\\n", "<br/>"));
				
				Text desc = new Text(plugin.getPluginDescription().replace("\\n", "\n"));
				desc.getStyleClass().add("list-plugin-desc");
				
				box.getChildren().addAll(desc);

				return new SimpleObjectProperty<>(box);
			}
		});
		
		TableColumn<GedWorkerPlugin, VBox> colMang = new TableColumn<>(properties.getProperty("action"));
		colMang.setCellValueFactory(new Callback<CellDataFeatures<GedWorkerPlugin, VBox>, ObservableValue<VBox>>() {
			public ObservableValue<VBox> call(CellDataFeatures<GedWorkerPlugin, VBox> p) {
				VBox box = new VBox();

				final GedWorkerPlugin pmi = p.getValue();

                Button btnActivate = new Button(properties.getProperty("run"));
                btnActivate.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        eventHandler.pluginActionRequired(WorkerPluginScreenEventHandler.Action.ONE_SHOT_RUN, pmi);
                    }
                });

                box.getChildren().addAll(btnActivate);


				return new SimpleObjectProperty<>(box);
			}
		});
		
		
		table.getColumns().add(colName);
		table.getColumns().add(colDesc);
		table.getColumns().add(colMang);
		
		colName.prefWidthProperty().bind(table.widthProperty().multiply(0.28));
		colDesc.prefWidthProperty().bind(table.widthProperty().multiply(0.59));
		colMang.prefWidthProperty().bind(table.widthProperty().multiply(0.12));
	}

	
	@Override
	public void refresh() {
		this.refreshPluginListContent();
	}
	
	/**
	 * Fill the table with the plugin list
	 */
	public void refreshPluginListContent() {

		pluginsList.clear();
		
		List<GedWorkerPlugin> plugins = PluginManager.getWorkerPluginList();

		logger.info("Plugin count : " + plugins.size());

		// show only activated plugins
		for (GedWorkerPlugin p : plugins) {
			pluginsList.add(p);
		}
	}
}
