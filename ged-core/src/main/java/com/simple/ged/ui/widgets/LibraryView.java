package com.simple.ged.ui.widgets;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.Profile;
import com.simple.ged.models.GedDirectory;
import com.simple.ged.services.GedDirectoryService;
import com.simple.ged.services.GedDocumentService;
import com.simple.ged.ui.screen.DirectoryEditionScreen;
import com.simple.ged.ui.screen.SoftwareScreen;
import com.simple.ged.ui.widgets.eventhandler.LibraryViewEventHandler;

import fr.xmichel.toolbox.tools.FileHelper;
import fr.xmichel.toolbox.tools.PropertiesHelper;

/**
 * A view of the library, a tree with files in GED directory
 * 
 * @author xavier
 */

public class LibraryView extends TreeView<String> {

	/**
	 * Tree item icon size (px)
	 */
	public static final int TREE_ITEM_SIZE = 30;
	
	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(LibraryView.class);
	
	/**
	 * Some facking (yes it's a 'A') node which is a fack children (for expandable property)
	 */
	public static final TreeItem<String> FACK_CHILD = new TreeItem<String>("");
	
	/**
	 * Have I to show all files or just directories ? 
	 * Default : false
	 */
	private boolean showDirectoryOnly;
	
	/**
	 * My event handler
	 */
	private LibraryViewEventHandler eventHandler;
	
	/**
	 * The software properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
	/**
	 * My parent
	 */
	private WeakReference<SoftwareScreen> parentScreen;
	
	/**
	 * Constructor. Will show directories content
	 * 
	 * @param parentScreen
	 * 				My parent
	 */
	public LibraryView(SoftwareScreen parentScreen) {
		this(parentScreen, false);
    }
    
	/**
	 * Constructor
	 * 
	 * @param parentScreen
	 * 				My parent
	 * 
	 * @param showDirectoryOnly
	 *				Must I only show directories ? (not there content = files)
	 */
	public LibraryView(SoftwareScreen parentScreen, boolean showDirectoryOnly) {
		this.showDirectoryOnly = showDirectoryOnly;
		
    	this.parentScreen = new WeakReference<>(parentScreen);
    	
    	buildTree();
    	
    	eventHandler = new LibraryViewEventHandler(this);
    	
    	this.setEditable(true);
    	this.setCellFactory(eventHandler);
    	this.getSelectionModel().select(this.getRoot());

    	this.getSelectionModel().selectedItemProperty().addListener(eventHandler); 
	}
    
	/**
	 * Get the path to display in a tree node
	 */
	public static String convertToNodeName(String path) {
		String unixPath = GedDocumentService.forceUnixSeparator(path);
		String[] files = unixPath.split("/");
		return files[files.length - 1];
	}
    
    
    /**
	 * Build the library tree, according to the registered library root
	 */
	public void buildTree() {
		
		logger.info("Build or rebuild tree");
		
		if (getRoot() == null) {
			TreeItem<String> newRoot = new TreeItem<>(LibraryView.convertToNodeName(Profile.getInstance().getLibraryRoot()), getIconForNode(""));
			setRoot(newRoot);
		}

		logger.info("Building tree, root defined");
		
		getRoot().getChildren().clear();

        getRoot().addEventHandler(TreeItem.branchExpandedEvent(), new EventHandler<TreeItem.TreeModificationEvent<Object>>() { // object is string...
            @SuppressWarnings("unchecked")
            @Override
            public void handle(TreeModificationEvent<Object> arg0) { // object is string...
                logger.trace("Want to expand {}", arg0);
                eventHandler.branchExpandedEventHandler(arg0.getSource());
            }
        });


		loadAndAddChildrenUnderNode(new File(Profile.getInstance().getLibraryRoot()), getRoot());

		logger.info("Build or rebuild tree over");
	}
	
	
	/**
	 * The given path is absolute
	 */
 	public Node getIconForNode(String filePath) {
 		
 		logger.trace(filePath);
 		
 		// root
 		if (filePath.isEmpty()) {
			Image i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_library_root")));
			ImageView iv = new ImageView(i);
			iv.setSmooth(true);
			iv.setFitWidth(TREE_ITEM_SIZE);
			iv.setFitHeight(TREE_ITEM_SIZE);
			return iv;
 		}
 		
 		// folder
 		if (new File(filePath).isDirectory()) {
 			
 			logger.trace("ged icon for directory : {}", GedDocumentService.getRelativeFromAbsolutePath(filePath));
 			
 			GedDirectory dir = GedDirectoryService.findDirectorybyDirectoryPath(GedDocumentService.getRelativeFromAbsolutePath(filePath));
 			
 			ImageView iv = null;
 			
 			// no customization, default icon
 			if (dir == null) {
				Image i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_library_folder")));
				 iv = new ImageView(i);
 			}
 			else {
	 			// icon is customized
 				URL iconFileUrl = null;
				try {
					iconFileUrl = new File(DirectoryEditionScreen.DIRECTORY_ICON_DIRECTORY + dir.getIconPath()).toURI().toURL();
				} catch (MalformedURLException e) {
					logger.error("Fail to read custom icon url", e);
				}
 				logger.debug("custom image file : {}", iconFileUrl.toString());
	 			Image i = new Image(iconFileUrl.toString());
	 			iv = new ImageView(i);
 			}
 			
			iv.setSmooth(true);
			iv.setFitWidth(TREE_ITEM_SIZE);
			iv.setFitHeight(TREE_ITEM_SIZE);
			return iv;
 		}
 		
 		if (new File(filePath).isFile()) {
 			
 			Image i = null;
 			
 			switch (FileHelper.getFileType(filePath)) {
			case PDF_TYPE:
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_pdf")));
				break;
			case HTML_TYPE:
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_html")));
				break;
			case TEXT_TYPE :
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_txt")));
				break;
			case PPT_TYPE :
			case PPTX_TYPE :
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_ppt")));
				break;
			case DOC_TYPE :
			case DOCX_TYPE :
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_doc")));
				break;
			case PNG_TYPE :
			case JPG_TYPE :
			case BMP_TYPE :
			case GIF_TYPE :
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_img")));
				break;
			default:
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_unknown")));
				break;
			}
 			
 			// apply image
			ImageView iv = new ImageView(i);
			iv.setSmooth(true);
			iv.setFitWidth(TREE_ITEM_SIZE);
			iv.setFitHeight(TREE_ITEM_SIZE);
			return iv;
 		}

 		return null;
 	}
	
 	
 	
	/**
	 * List files under the given node
	 * 
	 * if node is a file, returns an empty list
	 */
	public void loadAndAddChildrenUnderNode(File file, TreeItem<String> node) {
		if (file.isFile()) {
			return;
		}
		for (File f : file.listFiles()) {

			TreeItem<String> subNode = new TreeItem<>(convertToNodeName(f.getName()), getIconForNode(f.getPath()));
			
			if (f.isDirectory()) {
				subNode.getChildren().add(FACK_CHILD);
			} else { // f is child
				if (showDirectoryOnly) {
					continue;
				}
			}
			
			node.getChildren().add(subNode);
		}
	}
 	

	public LibraryViewEventHandler getEventHandler() {
		return eventHandler;
	}
  

    public SoftwareScreen getParentScreen() {
		return parentScreen.get();
	}

	
}
