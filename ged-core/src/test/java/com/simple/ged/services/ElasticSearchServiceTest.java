package com.simple.ged.services;

import com.simple.ged.models.GedDocument;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.elasticsearch.node.Node;

public class ElasticSearchServiceTest {

    /**
     * Usefull documents for testing
     */
    private GedDocument docBac;
    private GedDocument docBrevet;
    private GedDocument docBacNotes;

    @Before
    public void setUp() throws Exception {
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

        GedDocumentService.addOrUpdateDocument(docBac);
        GedDocumentService.addOrUpdateDocument(docBrevet);
        GedDocumentService.addOrUpdateDocument(docBacNotes);
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

        ElasticSearchService.removeAllIndexedData();
        ElasticSearchService.indexDocument(docBac);
        ElasticSearchService.indexDocument(docBrevet);
        ElasticSearchService.indexDocument(docBacNotes);

        List<GedDocument> docs = ElasticSearchService.basicSearch("diplôme");

        List<GedDocument> attemptedResult = Arrays.asList(new GedDocument[]{
                docBac, docBrevet
        });

        Assert.assertNotNull(docs);
        Assert.assertTrue(docs.size() == attemptedResult.size());
        Assert.assertTrue(docs.containsAll(attemptedResult));
    }

    /**
     * Search test with ending 's'
     */
    @Test
    public void searchDocumentWithSTest() throws Exception {

        ElasticSearchService.removeAllIndexedData();
        ElasticSearchService.indexDocument(docBac);
        ElasticSearchService.indexDocument(docBrevet);
        ElasticSearchService.indexDocument(docBacNotes);

        List<GedDocument> docs = ElasticSearchService.basicSearch("diplômes");

        List<GedDocument> attemptedResult = Arrays.asList(new GedDocument[]{
                docBac, docBrevet
        });

        Assert.assertNotNull(docs);
        Assert.assertTrue(docs.size() == attemptedResult.size());
        Assert.assertTrue(docs.containsAll(attemptedResult));
    }

    /**
     * Search on multi word
     */
    @Test
    public void searchMultiWordDocumentTest() throws Exception {

        ElasticSearchService.removeAllIndexedData();
        ElasticSearchService.indexDocument(docBac);
        ElasticSearchService.indexDocument(docBrevet);
        ElasticSearchService.indexDocument(docBacNotes);

        List<GedDocument> docs = ElasticSearchService.basicSearch("diplôme bac");

        List<GedDocument> attemptedResult = Arrays.asList(new GedDocument[]{
                docBac
        });

        Assert.assertNotNull(docs);
        Assert.assertTrue(docs.size() == attemptedResult.size());
        Assert.assertTrue(docs.containsAll(attemptedResult));
    }


}
