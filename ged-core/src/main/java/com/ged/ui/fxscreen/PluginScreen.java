package com.ged.ui.fxscreen;

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

import org.apache.log4j.Logger;

import com.ged.connector.plugins.SimpleGedPlugin;
import com.ged.plugins.PluginManagementInformations;
import com.ged.plugins.PluginManager;
import com.ged.ui.FxMainWindow;
import com.ged.ui.fxscreen.eventhandler.PluginScreenEventHandler;
import com.tools.DateHelper;

/**
 * 
 * The plugin management screen allow you to enable/disable some plugin
 * 
 * @author xavier
 * 
 */
public class PluginScreen extends FxSoftwareScreen {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(PluginScreen.class);

	/**
	 * The table view contains the list of plugins
	 */
	private TableView<PluginManagementInformations> table;

	/**
	 * The plugin list
	 */
	private ObservableList<PluginManagementInformations> pluginsList;

	/**
	 * My event handler
	 */
	private PluginScreenEventHandler eventHandler;
	
	
	public PluginScreen(FxMainWindow mw) {
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

		eventHandler = new PluginScreenEventHandler(this);
		
		pluginsList = FXCollections.observableArrayList();
		table = new TableView<>();
	
		TableColumn<PluginManagementInformations, VBox> colName = new TableColumn<>(properties.getProperty("plugin_name"));
		colName.setCellValueFactory(new Callback<CellDataFeatures<PluginManagementInformations, VBox>, ObservableValue<VBox>>() {
			public ObservableValue<VBox> call(CellDataFeatures<PluginManagementInformations, VBox> p) {
				VBox box = new VBox();

				SimpleGedPlugin plugin = p.getValue().getPlugin();
				
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

		TableColumn<PluginManagementInformations, VBox> colDesc = new TableColumn<>(properties.getProperty("plugin_description"));
		colDesc.setCellValueFactory(new Callback<CellDataFeatures<PluginManagementInformations, VBox>, ObservableValue<VBox>>() {
			public ObservableValue<VBox> call(CellDataFeatures<PluginManagementInformations, VBox> p) {
				VBox box = new VBox();

				PluginManagementInformations pmi = p.getValue();
				SimpleGedPlugin plugin = pmi.getPlugin();
				
				Text desc = new Text(plugin.getPluginDescription());
				desc.getStyleClass().add("list-plugin-desc");
				
				box.getChildren().addAll(desc);
				
				if (p.getValue().isActivated()) {
					
					Text desc2 = new Text(
							properties.getProperty("actionned_on") + " " + pmi.getDayOfMonthForUpdate()
							+ " " + (pmi.getIntervalBetweenUpdates() == 1 ? properties.getProperty("each_month") : properties.getProperty("every") + " " + pmi.getIntervalBetweenUpdates() + properties.getProperty("month"))
							+ "\n"
							+ properties.getProperty("last_action") + " " + (pmi.getLastUpdateDate() != null ? properties.getProperty("the") + " " + DateHelper.calendarToString(pmi.getLastUpdateDate()) : " " + properties.getProperty("never"))
					);

					box.getChildren().addAll(new Separator(), desc2);
				}

				return new SimpleObjectProperty<>(box);
			}
		});
		
		TableColumn<PluginManagementInformations, VBox> colMang = new TableColumn<>(properties.getProperty("action"));
		colMang.setCellValueFactory(new Callback<CellDataFeatures<PluginManagementInformations, VBox>, ObservableValue<VBox>>() {
			public ObservableValue<VBox> call(CellDataFeatures<PluginManagementInformations, VBox> p) {
				VBox box = new VBox();

				final PluginManagementInformations pmi = p.getValue();

				if (pmi.isActivated()) {
					
					Button btnDesactivate = new Button(properties.getProperty("desactivate"));
					btnDesactivate.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							eventHandler.pluginActionRequired(PluginScreenEventHandler.Action.DESACTIVATE, pmi);
						}
					});
					
					Button btnModify = new Button(properties.getProperty("modify"));
					btnModify.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							eventHandler.pluginActionRequired(PluginScreenEventHandler.Action.MODIFY, pmi);
						}
					});
					
					box.getChildren().addAll(btnDesactivate, btnModify);
					
				}
				else { // pmi is not activated
					
					Button btnActivate = new Button(properties.getProperty("activate"));
					btnActivate.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							eventHandler.pluginActionRequired(PluginScreenEventHandler.Action.ACTIVATE, pmi);
						}
					});
					
					box.getChildren().addAll(btnActivate);
				}

				return new SimpleObjectProperty<>(box);
			}
		});
		
		
		table.getColumns().addAll(colName, colDesc, colMang);
	}

	/**
	 * Fill the table with the plugin list
	 */
	public void refreshPluginListContent() {

		pluginsList.clear();
		
		List<PluginManagementInformations> plugins = PluginManager.getPluginList();

		logger.info("Plugin count : " + plugins.size());

		// show only activated plugins
		for (PluginManagementInformations p : plugins) {
			if (p.isActivated()) {
				pluginsList.add(p);
			}
		}

		// show non activated plugins
		for (PluginManagementInformations p : plugins) {
			if (!p.isActivated()) {
				pluginsList.add(p);
			}
		}

	}
}
