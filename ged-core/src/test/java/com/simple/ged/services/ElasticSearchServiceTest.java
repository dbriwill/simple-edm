package com.simple.ged.services;

import com.simple.ged.models.GedDocument;
import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Date;

import org.elasticsearch.node.Node;

public class ElasticSearchServiceTest {

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
        GedDocument doc = new GedDocument();
        doc.setId(123456);
        doc.setDate(new Date());
        doc.setDescription("Document description");
        doc.setName("Document name");

        ElasticSearchService.removeAllIndexedData();
        Assert.assertFalse(ElasticSearchService.documentIsIndexed(doc));

        ElasticSearchService.indexDocument(doc);
        Assert.assertTrue(ElasticSearchService.documentIsIndexed(doc));

        ElasticSearchService.unindexDocument(doc);
        Assert.assertFalse(ElasticSearchService.documentIsIndexed(doc));
    }
}
