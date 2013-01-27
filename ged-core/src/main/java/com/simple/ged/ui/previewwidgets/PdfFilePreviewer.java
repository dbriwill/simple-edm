package com.simple.ged.ui.previewwidgets;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Dimension2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.jpedal.PdfDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.Profile;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPrintPage;

import fr.xmichel.javafx.dialog.Dialog;

/**
 * 
 * 
 * @author xavier
 * 
 *         PDF file previewer, adapted from
 *         http://www.idrsolutions.com/jpedalfx-viewer/
 * 
 */
public class PdfFilePreviewer extends AbstractFilePreviewer {

	private static final Logger logger = LoggerFactory.getLogger(PdfFilePreviewer.class);
	
	private PdfDecoder pdf;
	private ImageView imageView;
	private Button back, next;
	private List<Button> buttonGroup;
	private int pageNumber = 1;
	// Variables for making buttons disappear
	private long scheduleDisappearTime = System.currentTimeMillis();
	private Thread visibilityThread;


	public PdfFilePreviewer(String absoluteFilePath, Dimension2D maxSize) {
		super(absoluteFilePath, maxSize);
	}
	
	
	@Override
	public void closeFile() {
		logger.debug("Need to close pdf file");
		if (pdf.isOpen()) {
			pdf.closePdfFile();
			logger.debug("Closed, you can go on");
		}
	}
	

	@Override
	public void load() throws CannotCreatePreviewerException {

		pdf = new PdfDecoder();

		this.setPrefSize(maxSize.getWidth(), maxSize.getHeight());
		AnchorPane anchor = new AnchorPane();
		this.getChildren().add(anchor);
		anchor.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: black;");

		imageView = new ImageView();
		AnchorPane.setLeftAnchor(imageView, 0.0);
		AnchorPane.setTopAnchor(imageView, 0.0);
		AnchorPane.setRightAnchor(imageView, 1.0);
		AnchorPane.setBottomAnchor(imageView, 1.0);

		back = new Button("<");
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				showPage(pageNumber - 1);
			}
		});
		back.setPrefSize(50, 50);
		AnchorPane.setLeftAnchor(back, 3.0);
		AnchorPane.setBottomAnchor(back, 4.0);

		next = new Button(">");
		next.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				showPage(pageNumber + 1);
			}
		});
		next.setPrefSize(50, 50);
		AnchorPane.setRightAnchor(next, 4.0);
		AnchorPane.setBottomAnchor(next, 4.0);

		// Add buttons to list
		buttonGroup = new ArrayList<>();
		buttonGroup.add(next);
		buttonGroup.add(back);

		// Set buttons to transparent
		setButtonOpacities(0.0);

		// Set up mouse listener & add to components
		MouseListener listener = new MouseListener();
		imageView.addEventHandler(MouseEvent.ANY, listener);
		back.addEventHandler(MouseEvent.ANY, listener);
		next.addEventHandler(MouseEvent.ANY, listener);

		// Add components
		anchor.getChildren().addAll(imageView, next, back);

		// Check parameters for file to open - if not show chooser
		openFile(new File(absoluteFilePath));

	}

	double lastOpacity = 0.0; // Stores the last opacity value
	long dueToEnd = -1; // Stores the time when the last started
						// transition was due to end

	/**
	 * Fade the buttons opacities to a given level.
	 * 
	 * @param op
	 */
	private void setButtonOpacities(double op) {
		final int DUR = 250;

		// Avoid starting new transition during old transition
		if (dueToEnd != -1 && System.currentTimeMillis() < dueToEnd) {
			return;
		}

		for (Button n : buttonGroup) {
			FadeTransition f = new FadeTransition(Duration.millis(250), n);
			f.setFromValue(lastOpacity);
			f.setToValue(op);
			f.play();
		}

		// Store when transition should end so we know when to allow new ones
		dueToEnd = System.currentTimeMillis() + DUR;

		lastOpacity = op;
	}

	/**
	 * Open a file
	 * 
	 * @param file
	 * @return true if failed
	 */
	private boolean openFile(File file) {
		if (file == null) {
			return true;
		}

		String filename = file.getAbsolutePath();

		try {
			pdf.openPdfFile(filename);
			showPage(1);
		} catch (Exception e) {
			return true;
		}

		return false;
	}

	/**
	 * Update the GUI to show a specified page.
	 * 
	 * @param page
	 */
	private void showPage(int page) {

		// Check in range
		if (page > pdf.getPageCount()) {
			return;
		}
		if (page < 1) {
			return;
		}

		// Store
		pageNumber = page;

		// Show/hide buttons as neccessary
		if (page == pdf.getPageCount()) {
			next.setVisible(false);
		} else {
			next.setVisible(true);
		}
		
		if (page == 1) {
			back.setVisible(false);
		} else {
			back.setVisible(true);
		}
		
		// Calculate scale
		int pW = pdf.getPdfPageData().getCropBoxWidth(page);
		int pH = pdf.getPdfPageData().getCropBoxHeight(page);

		Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
		s.setSize(maxSize.getWidth(), maxSize.getHeight());

		double xScale = (double) s.width / pW;
		double yScale = (double) s.height / pH;
		double scale = xScale < yScale ? xScale : yScale;

		// Work out target size
		pW *= scale;
		pH *= scale;

		// Get image and set
		Image i = getPageAsImage(page, pW, pH);
		imageView.setImage(i);

		// Set size of components
		imageView.setFitWidth(pW);
		imageView.setFitHeight(pH);
	}

	/**
	 * Wrapper for usual method since JFX has no BufferedImage support.
	 * 
	 * @param page
	 * @param width
	 * @param height
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Image getPageAsImage(int page, int width, int height) {

		BufferedImage img;
		try {
			img = pdf.getPageAsImage(page);

			// Use deprecated method since there's no real alternative
			if (Image.impl_isExternalFormatSupported(BufferedImage.class)) {
				return javafx.scene.image.Image.impl_fromExternalImage(img);
			}

		} catch (Exception e) {
			logger.error("Cannot display pdf", e);
			Dialog.showThrowable("Erreur", "Impossible d'afficher le pdf", e);
		}

		return null;
	}

	private class MouseListener implements EventHandler<MouseEvent> {

		public MouseListener() {
		}

		public void handle(MouseEvent e) {
			EventType<?> type = e.getEventType();
			if (type == MouseEvent.MOUSE_MOVED) {

				// Set time to disappear
				scheduleDisappearTime = System.currentTimeMillis() + 1700;

				// Make buttons visible
				setButtonOpacities(0.75);

				// If thread doesn't exist, create it
				if (visibilityThread == null) {
					visibilityThread = new Thread() {

						public void run() {
							// Keep comparing to scheduled time (which is
							// updated outside of thread)
							while (System.currentTimeMillis() < scheduleDisappearTime || next.isHover() || back.isHover()) {
								try {
									Thread.sleep(50);
								} catch (InterruptedException e) {
									logger.error("Cannot sleep !", e);
								}
							}

							// Make buttons disappear
							setButtonOpacities(0.0);

							// Destroy thread
							visibilityThread = null;

						}
					};
					visibilityThread.start();
				}
			}
		}
	}

	@Override
	public boolean isPrintable() {
		return true;
	}

	@Override
	public void print() {
		
		try {
			File input = new File(absoluteFilePath);
			FileInputStream fis = new FileInputStream(input);
			FileChannel fc = fis.getChannel();
			ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
	
			PDFFile curFile=null;
			PDFPrintPage pages=null;
			curFile = new PDFFile(bb); // Create PDF Print Page
			pages = new PDFPrintPage(curFile);
			PrinterJob pjob = PrinterJob.getPrinterJob();
	
			PrintService[] services = PrinterJob.lookupPrintServices();
			for(PrintService ps:services){
			    String pName = ps.getName();
			    if(pName.equals(Profile.getInstance().getDefaultPrinterName())){
			        pjob.setPrintService(ps);
			        logger.debug("Printing on : " + pName);
			        break;
			    }
			}
	
			pjob.setJobName(absoluteFilePath);
			Book book = new Book();
			PageFormat pformat = PrinterJob.getPrinterJob().defaultPage();
			book.append(pages, pformat, curFile.getNumPages());
			pjob.setPageable(book);
	
			// print
			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
	
	
			// Print it
			pjob.print(aset);
			fis.close();
		} catch (Exception e) {
			logger.error("Cannot print pdf", e);
			Dialog.showThrowable("Erreur", "Impossible d'imprimer le fichier", e);
		}
		
	}
}
