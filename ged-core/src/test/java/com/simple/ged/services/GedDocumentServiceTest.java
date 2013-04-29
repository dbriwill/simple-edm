package com.simple.ged.services;

import org.junit.Assert;
import org.junit.Test;

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

}
