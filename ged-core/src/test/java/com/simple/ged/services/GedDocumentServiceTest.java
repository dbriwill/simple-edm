package com.simple.ged.services;

import org.junit.Assert;
import org.junit.Test;

import com.simple.ged.Profile;

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
        
        Assert.assertEquals("toto/foo.txt", GedDocumentService.getRelativeFromAbsloutePath(windowPath));
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
        
        Assert.assertEquals("toto/foo.txt", GedDocumentService.getRelativeFromAbsloutePath(unixPath));
    }
}
