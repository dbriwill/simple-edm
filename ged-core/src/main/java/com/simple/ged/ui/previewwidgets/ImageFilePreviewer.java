package com.simple.ged.ui.previewwidgets;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.print.DocFlavor;

import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.tools.PrintingHelper;

import fr.xmichel.toolbox.tools.FileHelper;

/**
 * Some previewer for image files
 * 
 * @author xavier
 *
 */
public class ImageFilePreviewer extends AbstractFilePreviewer {

	private static final Logger logger = LoggerFactory.getLogger(ImageFilePreviewer.class);
	

	public ImageFilePreviewer(String absoluteFilePath, Dimension2D maxSize) {
		super(absoluteFilePath, maxSize);
	}

	@Override
	public void load() throws CannotCreatePreviewerException {
		try {
			File file = new File(absoluteFilePath);
			Image image = new Image(file.toURI().toURL().toExternalForm());

			Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
			s.setSize(maxSize.getWidth(), maxSize.getHeight());

			double pW = image.getWidth();
			double pH = image.getHeight();
			
			double xScale = (double) s.width / pW;
			double yScale = (double) s.height / pH;
			double scale = xScale < yScale ? xScale : yScale;

			if (scale > 1) { // we don't want a too big image
				scale = 1;
			}
			
			// Work out target size
			pW *= scale;
			pH *= scale;

			// Get image and set
			ImageView imageView = new ImageView(image);

			// Set size of components
			imageView.setSmooth(true);
			imageView.setFitWidth(pW);
			imageView.setFitHeight(pH);
			
			getChildren().add(imageView);
		} 
		catch (Exception e) {
			logger.error("Failed to load " + absoluteFilePath, e);
			throw new CannotCreatePreviewerException();
		}
	}

	@Override
	public boolean isPrintable() {

		switch (FileHelper.getFileType(absoluteFilePath)) {
		case PNG_TYPE :
		case JPG_TYPE :
		case GIF_TYPE :
			return true;
		default:
			return false;
		}
	}
	
	
	@Override
	public void print() {

		DocFlavor flavor = null;
		
		switch (FileHelper.getFileType(absoluteFilePath)) {
		case PNG_TYPE:
			flavor = DocFlavor.INPUT_STREAM.PNG;
			break;
		case JPG_TYPE:
			flavor = DocFlavor.INPUT_STREAM.JPEG;
			break;
		case GIF_TYPE:
			flavor = DocFlavor.INPUT_STREAM.GIF;
			break;
		default:
			return;
		}
		
		PrintingHelper.printFile(absoluteFilePath, flavor);
	}

}
