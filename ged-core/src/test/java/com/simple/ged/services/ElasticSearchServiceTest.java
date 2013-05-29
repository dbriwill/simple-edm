package com.simple.ged.services;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.elasticsearch.node.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.simple.ged.Profile;
import com.simple.ged.models.GedDocument;
import com.simple.ged.models.GedDocumentFile;

public class ElasticSearchServiceTest {

    /**
     * Usefull documents for testing
     */
    private GedDocument docBac;
    private GedDocument docBrevet;
    private GedDocument docBacNotes;
    private GedDocument docLatex;

    @Before
    public void setUp() throws Exception {
    	
    	String targetDirAbsolutePath = System.getProperty("user.dir") + (System.getProperty("user.dir").contains("ged-core") ? "" : "/ged-core") + "/target/";
    	Profile.getInstance().setDocumentLibraryRoot(targetDirAbsolutePath);
    	
        docBac = new GedDocument();
        docBac.setDate(new Date());
        docBac.setName("Diplome du bac");
        docBac.setDescription("Bla");


        docBrevet = new GedDocument();
        docBrevet.setDate(new Date());
        docBrevet.setName("Brevet");
        docBrevet.setDescription("diplôme du brevet");

        docBacNotes = new GedDocument();
        docBacNotes.setDate(new Date());
        docBacNotes.setName("Notes du bac");
        docBacNotes.setDescription("Relevé de notes du bac");

        docLatex = new GedDocument();
        docLatex.setDate(new Date());
        docLatex.setName("Un template de document...");
        docLatex.setDescription("...réalisé dans un format binaire");
        docLatex.addFile(new GedDocumentFile("test-classes/demo_pdf.pdf"));
        
        GedDocumentService.addOrUpdateDocument(docBac);
        GedDocumentService.addOrUpdateDocument(docBrevet);
        GedDocumentService.addOrUpdateDocument(docBacNotes);
        GedDocumentService.addOrUpdateDocument(docLatex);
        
        ElasticSearchService.removeAllIndexedData();
        ElasticSearchService.indexDocument(docBac);
        ElasticSearchService.indexDocument(docBrevet);
        ElasticSearchService.indexDocument(docBacNotes);
        ElasticSearchService.indexDocument(docLatex);
    }



    /**
     * Node is started and working
     */
    @Test
    public void nodeTest() throws Exception {
        Field field = ElasticSearchService.class.getDeclaredField("node");
        field.setAccessible(true);

        Node node = (Node)field.get(new ElasticSearchService());

        Assert.assertNotNull(node);
        Assert.assertFalse(node.isClosed());
    }

    /**
     * Index some document
     */
    @Test
    public void indexAndRemoveDocument() throws Exception {
        ElasticSearchService.removeAllIndexedData();
        Assert.assertFalse(ElasticSearchService.documentIsIndexed(docBrevet));

        ElasticSearchService.indexDocument(docBrevet);
        Assert.assertTrue(ElasticSearchService.documentIsIndexed(docBrevet));

        ElasticSearchService.unindexDocument(docBrevet);
        Assert.assertFalse(ElasticSearchService.documentIsIndexed(docBrevet));
    }

    /**
     * Search test with accented word
     */
    @Test
    public void searchDocumentWithAccentTest() throws Exception {
        List<GedDocument> docs = ElasticSearchService.basicSearch("diplôme");

        List<Integer> attemptedResult = Arrays.asList(new Integer[]{
                docBac.getId(), docBrevet.getId()
        });

        Assert.assertNotNull(docs);
        Assert.assertTrue(docs.size() == attemptedResult.size());
        for (GedDocument doc : docs) {
        	Assert.assertTrue(attemptedResult.contains(doc.getId()));
        }
        //Assert.assertTrue(docs.containsAll(attemptedResult));	// WTF ?
    }

    /**
     * Search test with ending 's'
     */
    @Test
    public void searchDocumentWithSTest() throws Exception {
        List<GedDocument> docs = ElasticSearchService.basicSearch("diplômes");

        List<Integer> attemptedResult = Arrays.asList(new Integer[]{
                docBac.getId(), docBrevet.getId()
        });

        Assert.assertNotNull(docs);
        Assert.assertTrue(docs.size() == attemptedResult.size());
        for (GedDocument doc : docs) {
        	Assert.assertTrue(attemptedResult.contains(doc.getId()));
        }
        //Assert.assertTrue(docs.containsAll(attemptedResult));	// WTF ?
    }

    /**
     * Search on multi word
     */
    @Test
    public void searchMultiWordDocumentTest() throws Exception {
        List<GedDocument> docs = ElasticSearchService.basicSearch("diplôme bac");

        List<Integer> attemptedResult = Arrays.asList(new Integer[]{
                docBac.getId()
        });
        
        Assert.assertNotNull(docs);
        Assert.assertTrue(docs.size() == attemptedResult.size());
        for (GedDocument doc : docs) {
        	Assert.assertTrue(attemptedResult.contains(doc.getId()));
        }
        //Assert.assertTrue(docs.containsAll(attemptedResult));	// WTF ?
    }


    /**
     * Search in document binary content
     */
    @Test
    public void searchOnBinaryContent() throws Exception {
    	List<GedDocument> docs = ElasticSearchService.basicSearch("latex");

        List<Integer> attemptedResult = Arrays.asList(new Integer[]{
                docLatex.getId()
        });

        Assert.assertNotNull(docs);
        Assert.assertTrue(docs.size() == attemptedResult.size());
        for (GedDocument doc : docs) {
        	Assert.assertTrue(attemptedResult.contains(doc.getId()));
        }
        //Assert.assertTrue(docs.containsAll(attemptedResult));	// WTF ?
    }

    /**
     * Search in document binary metadata (author)
     */
    @Test
    public void searchOnBinaryMetadataContent() throws Exception {
        List<GedDocument> docs = ElasticSearchService.basicSearch("xavier");

        List<Integer> attemptedResult = Arrays.asList(new Integer[]{
                docLatex.getId()
        });

        Assert.assertNotNull(docs);
        Assert.assertTrue(docs.size() == attemptedResult.size());
        for (GedDocument doc : docs) {
            Assert.assertTrue(attemptedResult.contains(doc.getId()));
        }
        //Assert.assertTrue(docs.containsAll(attemptedResult));	// WTF ?
    }


    /**
     * Test for indexing all documents at the first ES installation
     */
    @Test
    public void indexAllDocsInES() throws Exception {
        ElasticSearchService.removeAllIndexedData();

        List<GedDocument> docs = Arrays.asList(new GedDocument[]{
                docLatex, docBac, docBrevet, docBacNotes
        });

        ElasticSearchService.indexAllNonIndexedDocumentInLibrary();

        for (GedDocument doc : docs) {
            Assert.assertTrue(ElasticSearchService.documentIsIndexed(doc));
        }
    }

}
