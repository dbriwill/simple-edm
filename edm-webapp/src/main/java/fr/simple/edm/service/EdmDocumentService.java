package fr.simple.edm.service;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.Base64;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.common.base.CaseFormat;

import fr.simple.edm.ElasticsearchConfig;
import fr.simple.edm.model.EdmDocument;
import fr.simple.edm.model.EdmNode;
import fr.simple.edm.repository.EdmDocumentRepository;

@Service
@PropertySources(value = { @PropertySource("classpath:/edm-configuration.properties") })
public class EdmDocumentService {

    private final Logger logger = LoggerFactory.getLogger(EdmDocumentService.class);

    @Inject
    private Environment env;
    
    @Inject
    private ElasticsearchConfig elasticsearchConfig;

    @Inject
    private EdmDocumentRepository edmDocumentRepository;

    @Inject
    private EdmNodeService edmNodeService;

    
    public EdmDocument findOne(String id) {
        return edmDocumentRepository.findOne(id);
    }
    
    public EdmDocument save(EdmDocument edmDocument) {
        
        edmNodeService.moveNodeIfNecessary(edmDocument);

        try {

            // the document is build manualy to
            // have the possibility to add the binary file
            // content

            XContentBuilder contentBuilder = jsonBuilder();

            // add document attributes
            contentBuilder.startObject();

            Class<?>[] classes = new Class[] { EdmNode.class, EdmDocument.class };
            for (Class<?> clazz : classes) {
                for (Method m : clazz.getDeclaredMethods()) {
                    if (m.getName().startsWith("get")) {
                        if (m.getName().equalsIgnoreCase("getFilename")) { // ignore
                                                                           // this
                                                                           // type
                            continue;
                        }
                        Object oo = m.invoke(edmDocument);
                        String fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, m.getName().substring(3));
                        contentBuilder.field(fieldName, oo);
                    }
                }
            }

            if (!edmDocument.getFilename().isEmpty()) {
                
                // computed values
                String thisDocumentFileExtension = com.google.common.io.Files.getFileExtension(edmDocument.getFilename());
                edmDocument.setFileExtension(thisDocumentFileExtension);
                
                // file copy
                String from = edmDocument.getFilename();
                String to = edmNodeService.getServerFilePathOfDocument(edmDocument);
                logger.debug("Copy {} to {}", new File(from), new File(to));
                com.google.common.io.Files.createParentDirs(new File(to));
                com.google.common.io.Files.copy(new File(from), new File(to));
                
                // now add the file in ES
                logger.debug("Adding file '{}' for ES indexation", edmDocument.getFilename());

                contentBuilder.startObject("file");

                Path filePath = Paths.get(edmDocument.getFilename());
                
                String contentType = Files.probeContentType(filePath);
                String content = Base64.encodeBytes(Files.readAllBytes(filePath));

                contentBuilder.field("content", content);

                contentBuilder.endObject();
                
                contentBuilder.field("fileExtension", thisDocumentFileExtension);
                contentBuilder.field("fileContentType", contentType);
            }

            // and that's all folks
            contentBuilder.endObject();

            // TODO : dynamise index and type with EdmDocument annotation !
            IndexResponse ir = elasticsearchConfig.getClient().prepareIndex("documents", "document", edmDocument.getId()).setSource(contentBuilder).execute().actionGet();

            edmDocument.setId(ir.getId());

            logger.debug("Indexed edm document '{}' with id '{}'", edmDocument.getName(), edmDocument.getId());
        } catch (Exception e) {
            logger.error("Failed to index document", e);
        }

        return edmDocument;
    }

    public List<EdmDocument> search(String pattern) {
        BoolQueryBuilder qb = QueryBuilders.boolQuery();

        for (String word : pattern.trim().split(" ")) {
            qb.must(QueryBuilders.fuzzyLikeThisQuery("_all").likeText(word));
        }

        logger.debug("The search query for pattern '{}' is : {}", pattern, qb);

        return Lists.newArrayList(edmDocumentRepository.search(qb));
    }

    public List<EdmDocument> findByParent(String parentId) {
        return edmDocumentRepository.findByParentId(parentId);
    }

    public List<EdmDocument> findByName(String name) {
        return edmDocumentRepository.findByName(name);
    }
    
    public void delete(EdmDocument edmDocument) {
        edmDocumentRepository.delete(edmDocument);
    }

    /**
     * Convert the file path to a node path.
     * 
     * Actually, the idea is the file path has just document.fileExtension more than node path 
     */
    public String filePathToNodePath(String filePath) {
        return new File(filePath).getParent().replace("\\", "/") + "/" + com.google.common.io.Files.getNameWithoutExtension(filePath);
    }
        
    public EdmDocument findEdmDocumentByFilePath(String filePath) {
        String nodePath = filePathToNodePath(filePath);
        logger.debug("Get server file path for node path : '{}'", nodePath);
        return (EdmDocument) edmNodeService.findOneByPath(nodePath);
    }
    
    public String getServerPathOfEdmDocument(EdmDocument document) {
        return edmNodeService.getServerFilePathOfDocument(document);
    }
}
