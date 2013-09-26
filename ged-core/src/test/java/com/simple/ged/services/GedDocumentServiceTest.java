package com.simple.ged.services;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.simple.ged.Profile;
import com.simple.ged.models.GedDocument;
import com.simple.ged.models.GedDocumentFile;
import com.simple.ged.tools.SpringFactory;

/**
 * DocumentDAO tests
 */
public class GedDocumentServiceTest {

    /**
     * Les \ d'un chemin windows sont convertis en /
     */
    @Test
    public void testWindowsToUnixPath() {
        String windowPath = "C:\\temp\\test\\foo.txt";
        String unixPath = GedDocumentService.forceUnixSeparator(windowPath);
        Assert.assertEquals(unixPath, "C:/temp/test/foo.txt");
    }

    /**
     * La règle précédente vaut aussi pour les doubles \\\\ qui se transforme en simple /
     */
    @Test
    public void testWindowsToUnixPathMultiBackslash() {
        String windowPath = "C:\\temp\\test\\foo.txt";
        String unixPath = GedDocumentService.forceUnixSeparator(windowPath);
        Assert.assertEquals(unixPath, "C:/temp/test/foo.txt");
    }


    /**
     * Un chemin linux reste valide
     */
    @Test
    public void testKeepValidUnixPath() {
        String unixPath = "/tmp/test/foo.txt";
        Assert.assertEquals(unixPath, GedDocumentService.forceUnixSeparator(unixPath));
    }

    
    /**
     *  absolu pour windows
     */
    @Test
    public void testRealtiveWindowsPath() {
        String windowPath = "C:\\tmp\\test\\toto\\foo.txt";
        String gedRoot  = "C:\\tmp\\test";
        
        Profile.getInstance().setDocumentLibraryRoot(gedRoot);
        Profile.getInstance().commitChanges();
        
        Assert.assertEquals("toto/foo.txt", GedDocumentService.getRelativeFromAbsolutePath(windowPath));
    }
    
    /**
     * Chemin relatif pour linux
     */
    @Test
    public void testRealtiveLinuxPath() {
        String unixPath = "/tmp/test/toto/foo.txt";
        String gedRoot  = "/tmp/test/";
        
        Profile.getInstance().setDocumentLibraryRoot(gedRoot);
        Profile.getInstance().commitChanges();
        
        Assert.assertEquals("toto/foo.txt", GedDocumentService.getRelativeFromAbsolutePath(unixPath));
    }
    
    
    /**
     * La fonction de recherche JPA fonctionne
     */
    @Test
    public void testFindByRelativeFilePath() {
    	String targetDirAbsolutePath = System.getProperty("user.dir") + (System.getProperty("user.dir").contains("ged-core") ? "" : "/ged-core") + "/target/";
    	Profile.getInstance().setDocumentLibraryRoot(targetDirAbsolutePath);
    	
    	GedDocumentService gedDocumentService = SpringFactory.getAppContext().getBean(GedDocumentService.class);
    	
    	GedDocument doc = new GedDocument();
    	doc.setDate(new Date());
    	doc.setName("Diplome du bac");
    	doc.setDescription("Bla");
    	doc.addFile(new GedDocumentFile("test-classes/demo_pdf.pdf"));
  
    	gedDocumentService.save(doc);
  
    	GedDocument docFound = gedDocumentService.findDocumentByFilePath("test-classes/demo_pdf.pdf");
    	
    	Assert.assertNotNull(docFound);
    	Assert.assertEquals(doc, docFound);
    }
}
