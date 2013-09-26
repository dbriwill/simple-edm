package com.simple.ged.ui.screen;

import java.util.HashMap;
import java.util.Map;

import com.simple.ged.connector.plugins.dto.SimpleGedPluginPropertyDTO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.connector.plugins.dto.SimpleGedPluginPropertyDTO;
import com.simple.ged.models.GedWorkerPlugin;
import com.simple.ged.ui.MainWindow;
import com.simple.ged.ui.screen.eventhandler.WorkerPluginConfigurationScreenEventHandler;

import fr.xmichel.toolbox.tools.DateHelper;


/**
 * 
 * This is the screen for configuring worker plugins
 * 
 * @author xavier
 *
 */
public class WorkerPluginConfigurationScreen extends SoftwareScreen {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(WorkerPluginConfigurationScreen.class);
	
	/**
	 * The library view, to chose document target
	 */
	//private LibraryView libraryView;
	
	/**
	 * The concerned plugin
	 */
	private GedWorkerPlugin plugin;
	
	/**
	 * The plugin name
	 */
	private Label title;
	
	/**
	 * The plugin version
	 */
	private Label version;

	/**
	 * The plugin author
	 */
	private Label author;
	
	/**
	 * The plugin jar name
	 */
	private Label jarName;
	
	/**
	 * The plugin description
	 */
	private Text desc;
	
	/**
	 * Help text
	 */
	//private Text help;
	
	/**
	 * Save button
	 */
	private Button save;
	
	/**
	 * The options layout (will be fill after receiving extra values)
	 */
	private GridPane optionLayout;
	
	/**
	 * The day selector
	 */
	//private ComboBox<Integer> comboDayOfMonthForUpdate;
	
	/**
	 * The interval between update selector
	 */
	//private ComboBox<Integer> comboIntervalBetweenUpdateInMonth;
	
	/**
	 * Map of properties
	 */
	private Map<SimpleGedPluginPropertyDTO, Control> propertiesFieldsMap;
	
	/**
	 * The destination file name pattern
	 */
	//private TextField fieldNamePattern;
	
	/**
	 * My event handler
	 */
	private WorkerPluginConfigurationScreenEventHandler eventHandler;
	
	
	public WorkerPluginConfigurationScreen(MainWindow mw) {
		super(mw);
		
		instanciateWidgets();
		
		GridPane globalLayout = new GridPane();
		globalLayout.setHgap(20);
		globalLayout.setVgap(20);

		VBox detailsBox = new VBox();
		detailsBox.getChildren().addAll(title, version, author, new Separator(), jarName);
		
//		optionLayout.add(new Label(properties.getProperty("destination_file_name")), 0, 0);
//		optionLayout.add(fieldNamePattern, 1, 0);
//
//		optionLayout.add(new Label(properties.getProperty("day_of_getting")), 0, 1);
//		optionLayout.add(comboDayOfMonthForUpdate, 1, 1);
//		
//		optionLayout.add(new Label(properties.getProperty("time_between")), 0, 2);
//		optionLayout.add(comboIntervalBetweenUpdateInMonth, 1, 2);
		
		VBox optionsBox = new VBox(30);
//		optionsBox.getChildren().addAll(help, optionLayout);
		optionsBox.getChildren().addAll(optionLayout);
		
		globalLayout.add(detailsBox, 0, 0);
		globalLayout.add(desc, 1, 0);
		//globalLayout.add(libraryView, 0, 1);
		globalLayout.add(optionsBox, 1, 1);
		globalLayout.add(save, 1, 2);
		
		this.getChildren().addAll(globalLayout);
	}
	
	
	@Override
	public void pullExtraValues(Map<String, Object> extras) {
		plugin = (GedWorkerPlugin) extras.get("ged-plugin");
		
		title.setText(plugin.getPlugin().getPluginName());
		version.setText(properties.getProperty("Version") + " " + plugin.getPlugin().getPluginVersion() + " " + properties.getProperty("released_the") + " " + DateHelper.calendarToString(plugin.getPlugin().getPluginDate()));
		author.setText((properties.getProperty("by") + " " + plugin.getPlugin().getPluginAuthor()));
		jarName.setText(plugin.getPlugin().getJarFileName());
		desc.setText(plugin.getPlugin().getPluginDescription().replace("\\n", "\n"));
		
		
//		if (plugin.isActivated()) {
//			fieldNamePattern.setText(plugin.getDestinationFilePattern());
//			comboDayOfMonthForUpdate.getSelectionModel().select(plugin.getDayOfMonthForUpdate() - 1);
//			comboIntervalBetweenUpdateInMonth.getSelectionModel().select(plugin.getIntervalBetweenUpdates() - 1);
//		}
		
		
		int currentRowNumber = 3;


		for (SimpleGedPluginPropertyDTO property : plugin.getPlugin().getProperties()) {
			
			Control field = null;
			
			if (property.isBooleanProperty()) {
				
				field = new CheckBox();
				((CheckBox) field).setSelected(property.getBooleanValue());
				
//				if (plugin.isActivated()) {
//					logger.debug("Plugin is activated, setting value");
//					for (SimpleGedPluginProperty pr : plugin.getPluginProperties()) {
//						if (pr.getPropertyKey().equals(property.getPropertyKey())) {
//							((CheckBox) field).setSelected(pr.getBooleanValue());
//							break;
//						}
//					}
//				}
				
			}
			else { // property is not a boolean
			
				field = new TextField();
				if (property.isHidden()) {
					field = new PasswordField();
				}
			
//				if (plugin.isActivated()) {
//					logger.debug("Plugin is activated, setting value");
//					for (SimpleGedPluginProperty pr : plugin.getPluginProperties()) {
//						if (pr.getPropertyKey().equals(property.getPropertyKey())) {
//							((TextField) field).setText(pr.getPropertyValue());
//							break;
//						}
//					}
//				}
				
				field.setOnKeyReleased(eventHandler);
				
			}
			
			propertiesFieldsMap.put(property, field);
			
			optionLayout.add(new Label(property.getPropertyLabel()), 0, currentRowNumber);
			optionLayout.add(field, 1, currentRowNumber);
			
			++currentRowNumber;
		}
	}


	private void instanciateWidgets() {
		
		eventHandler = new WorkerPluginConfigurationScreenEventHandler(this);
		
		
		//libraryView = new LibraryView(this, true);
		
		title = new Label();
		title.getStyleClass().add("list-plugin-title");
		
		version = new Label();
		version.getStyleClass().add("list-plugin-version");
		
		author = new Label();
		author.getStyleClass().add("list-plugin-author");
		
		jarName = new Label();
		jarName.getStyleClass().add("list-plugin-jarname");
		
		desc = new Text();
		desc.getStyleClass().add("list-plugin-desc");
		
//		help = new Text();
//		help.setText(properties.getProperty("plugin_option_user_guide"));
		
		save = new Button(properties.getProperty("run"));
		save.setPrefSize(250, 80);
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				eventHandler.setOnActionEvent(arg0);
			}
		});
		
		Image i3 = new Image(getClass().getResourceAsStream(properties.getProperty("ico_run")));
		ImageView iv3 = new ImageView(i3);
		iv3.setSmooth(true);
		iv3.setFitWidth(64);
		iv3.setFitHeight(64);
		save.setGraphic(iv3);
		
		save.setDisable(true);
		
		
		optionLayout = new GridPane();
		optionLayout.setHgap(10);
		optionLayout.setVgap(10);
		
//		comboDayOfMonthForUpdate = new ComboBox<>();
//		
//		List<Integer> vDays = new ArrayList<>();
//		for (int i = 1; i <= 30; ++i) {
//			vDays.add(i);
//		}
//		comboDayOfMonthForUpdate.getItems().addAll(vDays);
//		comboDayOfMonthForUpdate.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent arg0) {
//				eventHandler.checkValidity();
//			}
//		});
//		
//		comboIntervalBetweenUpdateInMonth = new ComboBox<>();
//		List<Integer> vMonth = new ArrayList<>();
//		for (int i = 1; i <= 12; ++i) {
//			vMonth.add(i);
//		}
//		comboIntervalBetweenUpdateInMonth.getItems().addAll(vMonth);
//		comboIntervalBetweenUpdateInMonth.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent arg0) {
//				eventHandler.checkValidity();
//			}
//		});
		
		propertiesFieldsMap = new HashMap<>();
		
//		fieldNamePattern = new TextField();
//		fieldNamePattern.setOnKeyReleased(eventHandler);
	}


//	public LibraryView getLibraryView() {
//		return libraryView;
//	}


	public GedWorkerPlugin getPlugin() {
		return plugin;
	}


	public Button getSave() {
		return save;
	}


//	public ComboBox<Integer> getComboDayOfMonthForUpdate() {
//		return comboDayOfMonthForUpdate;
//	}


//	public ComboBox<Integer> getComboIntervalBetweenUpdateInMonth() {
//		return comboIntervalBetweenUpdateInMonth;
//	}


	public Map<SimpleGedPluginPropertyDTO, Control> getPropertiesFieldsMap() {
		return propertiesFieldsMap;
	}


//	public TextField getFieldNamePattern() {
//		return fieldNamePattern;
//	}

}
