package com.simple.ged.services;

import static org.elasticsearch.common.io.Streams.copyToByteArray;
import static org.elasticsearch.common.io.Streams.copyToStringFromClasspath;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Base64;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.Profile;
import com.simple.ged.models.GedDocument;
import com.simple.ged.models.GedDocumentFile;

import fr.xmichel.toolbox.tools.FileHelper;

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
     * Index doc type
     *
     * @warning
     *          Keep updated doc-mapping.json !
     */
    private static final String ES_GED_INDEX_TYPE_DOC = "doc";

    /**
     * My logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);


    /**
     * Elastic search plugins folder
     */
    private static final String ES_PLUGIN_DIR = "es/plugins/";

    /**
     * Our ES node
     */
    private static Node node;

    static {
        // make sur plugin can be loaded before starting ES
        try {
            String mapperPluginAttachmentsDir = ES_PLUGIN_DIR + "mapper-attachments-1.7.0/";
            if (! FileHelper.folderExists(mapperPluginAttachmentsDir)) {
                logger.info("elasticsearch-mapper-attachments not deployed yet, I'll do it now !");
                com.simple.ged.tools.FileHelper.extractZipEmbeddedResource("/embedded/elasticsearch-mapper-attachments-1.7.0.zip", mapperPluginAttachmentsDir);
                logger.info("elasticsearch-mapper-attachments deployed");
            }
        }
        catch (Exception e) {
            logger.error("Failed to deploy elasticsearch-mapper-attachments plugin", e);
        }

        // now we can start ES
        node = NodeBuilder.nodeBuilder().node();
        node.client().admin().cluster().prepareHealth().setWaitForYellowStatus().execute().actionGet();

        rebuildEsIndexAndMappingsIfNecessary();
    }


    /**
     * Action to do on start or on ES restart
     */
    private static void rebuildEsIndexAndMappingsIfNecessary() {

        // recreate indexes if necessary
        try {
            node.client().admin().indices().prepareCreate(ES_GED_INDEX).execute().actionGet();
        }
        catch (IndexAlreadyExistsException e) {
            logger.info("Index {} already exists", ES_GED_INDEX);
        }
        catch (Exception e) {
            logger.error("Failed to rebuild index {}", ES_GED_INDEX, e);
        }

        // load custom mappings
        try {
            String content = copyToStringFromClasspath("/mapping/doc-mapping.json");
            node.client().admin().indices().preparePutMapping(ES_GED_INDEX).setType(ES_GED_INDEX_TYPE_DOC).setSource(content).execute().actionGet();
        } catch (IOException e) {
            logger.error("Failed to read doc mapping for ES", e);
        }

        node.client().admin().indices().refresh(new RefreshRequest(ES_GED_INDEX)).actionGet();
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

        rebuildEsIndexAndMappingsIfNecessary();
    }


    /**
     * Add the given document to ES
     *
     * @warning
     *          The document ID must be defined (for BDD mapping)
     */
    public static void indexDocument(GedDocument doc) {

        try {
        	
        	// the document is build manualy to 
        	// have the possibility to add the binary file
        	// content
        	
        	XContentBuilder contentBuilder = jsonBuilder();
        	
        	// add ged document attributes
        	contentBuilder.startObject();
        	
        	for (Method  m : GedDocument.class.getDeclaredMethods()) {
        		//contentBuilder = contentBuilder.field(f.getName(), f.get(doc));
        		if (m.getName().startsWith("get")) {
        			if (m.getName().equalsIgnoreCase("getDocumentFiles")) { // ignore this type
        				continue;
        			}
        			Object oo = m.invoke(doc);
        			contentBuilder.field(m.getName().substring(3), oo);
        		}
        	}
        	
        	// now add the binaries files
        	
        	if (doc.getDocumentFiles().size() > 0) {
        		
        		contentBuilder.startArray("files");
        		
        		for (GedDocumentFile gedDocumentFile : doc.getDocumentFiles()) {

                    logger.debug("Adding file '{}{}' for ES indexation", Profile.getInstance().getLibraryRoot(), gedDocumentFile.getRelativeFilePath());

                    contentBuilder.startObject();

        			Path filePath = Paths.get(Profile.getInstance().getLibraryRoot() + gedDocumentFile.getRelativeFilePath());
        			
        			String contentType = Files.probeContentType(filePath);
        			String name        = gedDocumentFile.getRelativeFilePath();
        			String content     = Base64.encodeBytes(Files.readAllBytes(filePath));
        			
        			contentBuilder.field("_content_type", contentType).field("_name", name).field("content", content);

                    contentBuilder.endObject();
        		}

                contentBuilder.endArray();
        	}

        	// and that's all folks
        	contentBuilder.endObject();

            IndexResponse ir = node.client().prepareIndex(ES_GED_INDEX, ES_GED_INDEX_TYPE_DOC, Integer.toString(doc.getId())).setSource(contentBuilder).execute().actionGet();

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
            GetResponse gr = node.client().prepareGet(ES_GED_INDEX, ES_GED_INDEX_TYPE_DOC, Integer.toString(doc.getId())).execute().actionGet();
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
            DeleteResponse dr = node.client().prepareDelete(ES_GED_INDEX, ES_GED_INDEX_TYPE_DOC, Integer.toString(doc.getId())).execute().get();
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

    /**
     * Search for the given words
     */
    public static List<GedDocument> basicSearch(String searched) {
        node.client().admin().indices().refresh(new RefreshRequest(ES_GED_INDEX)).actionGet();
    	
        List<GedDocument> documents = new ArrayList<>();

        BoolQueryBuilder qb = QueryBuilders.boolQuery();

        String[] lookFor = searched.split(" ");
        for (String word : lookFor) {
            qb.must(QueryBuilders.fuzzyLikeThisQuery("_all").likeText(word));
        }

        logger.debug("The query is : {}", qb);

        try {
            SearchResponse sr = node.client().prepareSearch(ES_GED_INDEX).setQuery(qb).setSize(10000).execute().actionGet();

            logger.debug("Matching docs count : {}", sr.getHits().getTotalHits());
            for (SearchHit hit : sr.getHits()) {
                // currently, we're not looking for the data stored in ES, just for the document ID
                documents.add(GedDocumentService.findDocumentById(Integer.parseInt(hit.getId())));
            }
        }
        catch (IndexMissingException e) {
            logger.error("Failed to search because the index {} doesn't exists yet !", ES_GED_INDEX);
        }
        catch (Exception e) {
            logger.error("Failed to search this pattern", e);
        }

        return documents;
    }


    /**
     * Index document which aren't indexed yet (I recommand to thread this method call)
     */
    public static void indexAllNonIndexedDocumentInLibrary() {
        logger.info("All document non indexed will be now");

        for (GedDocument doc : GedDocumentService.getAllDocuments()) {
            if (! documentIsIndexed(doc)) {
                logger.debug("Indexing document with id {}", doc.getId());
                indexDocument(doc);
            }
            else {
                logger.debug("Document {} is already indexed", doc.getId());
            }
        }

        logger.info("All document indexation over");
    }

}
