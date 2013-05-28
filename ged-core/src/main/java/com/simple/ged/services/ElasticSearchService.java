package com.simple.ged.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.ged.models.GedDocument;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Service for using Elastic search
 *
 * @author  Xavier
 */
public class ElasticSearchService {

    /**
     * Index name
     */
    private static final String ES_GED_INDEX = "ged";

    /**
     * My logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);

    /**
     * Our ES node
     */
    private static Node node;

    static {
        node = NodeBuilder.nodeBuilder().node();
        node.client().admin().cluster().prepareHealth().setWaitForYellowStatus().execute().actionGet();
    }

    /**
     * Remove all data in ES
     */
    public static void removeAllIndexedData() {
        try {
            DeleteIndexResponse delete = node.client().admin().indices().delete(new DeleteIndexRequest(ES_GED_INDEX)).actionGet();
            if (! delete.isAcknowledged()) {
                logger.error("Index wasn't deleted");
            }
            else {
                logger.info("Index has been deleted");
            }
        }
        catch (Exception e) {
            logger.error("Failed to delete index, has it been create yet ?");
        }

        // recreate indexes
        node.client().admin().indices().prepareCreate("ged").execute().actionGet();
        node.client().admin().indices().refresh(new RefreshRequest("ged")).actionGet();
    }


    /**
     * Add the given document to ES
     *
     * @warning
     *          The document ID must be defined (for BDD mapping)
     */
    public static void indexDocument(GedDocument doc) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String jsonString = mapper.writeValueAsString(doc);

            IndexResponse ir = null;
            ir = node.client().prepareIndex(ES_GED_INDEX, "doc", Integer.toString(doc.getId())).setSource(jsonString).execute().actionGet();

            logger.debug("Indexed ged document {} with id {}", doc.getId(), ir.getId());
        }
        catch (Exception e) {
            logger.error("Failed to index document", e);
        }
    }

    /**
     * You wanna know if some document is indexed ?
     */
    public static boolean documentIsIndexed(GedDocument doc) {
        try {
            GetResponse gr = node.client().prepareGet(ES_GED_INDEX, "doc", Integer.toString(doc.getId())).execute().actionGet();
            return (gr != null && gr.isExists());
        }
        catch (IndexMissingException e) {
            logger.error("Failed to check if document is indexed because the index doesn't exists yet");
        }
        catch (Exception e) {
            logger.error("Failed to check if document is indexed", e);
        }
        return false;
    }


    /**
     * Remove the given document from ES
     */
    public static void unindexDocument(GedDocument doc) {
        try {
            DeleteResponse dr = node.client().prepareDelete(ES_GED_INDEX, "doc", Integer.toString(doc.getId())).execute().get();
            if (dr.isNotFound()) {
                logger.warn("Failed to remove document : not found");
            }
            else {
                logger.debug("Unindexed document : {}", doc.getId());
            }
        }
        catch (Exception e) {
            logger.error("Failed to un-index document", e);
        }
    }
}
