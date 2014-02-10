package fr.simple.edm.service;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import fr.simple.edm.Application;
import fr.simple.edm.ElasticsearchTestingHelper;
import fr.simple.edm.common.EdmNodeType;
import fr.simple.edm.model.EdmDirectory;
import fr.simple.edm.model.EdmDocument;
import fr.simple.edm.model.EdmLibrary;
import fr.simple.edm.model.EdmNode;
import fr.simple.edm.service.EdmDirectoryService;
import fr.simple.edm.service.EdmDocumentService;
import fr.simple.edm.service.EdmLibraryService;
import fr.simple.edm.service.EdmNodeService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { Application.class })
@ComponentScan(basePackages = { "fr.simple.edm" })
public class GedNodeServiceTest {

    @Autowired
    private EdmNodeService gedNodeService;

    @Autowired
    private EdmLibraryService gedLibraryService;

    @Autowired
    private EdmDocumentService gedDocumentService;

    @Autowired
    private EdmDirectoryService gedDirectoryService;

    @Autowired
    private ElasticsearchTestingHelper elasticsearchTestingHelper;

    // testing id's
    private EdmLibrary gedLibrary;
    private String libraryId;

    private EdmDirectory gedDirectory;
    private String directoryId;

    private EdmDocument gedDocument;
    private String documentId;

    private EdmDirectory directoryWithDirectoryParent;
    private String directoryWithDirectoryParentId;

    private EdmDocument documentUnderLibrary;
    private String documentUnderLibraryId;

    /*
     * Faked library =============
     * 
     * % gedLibrary + gedDirectory - gedDocument + directoryWithDirectoryParent
     * - documentUnderLibrary
     */

    @Before
    public void setUp() throws Exception {
        elasticsearchTestingHelper.destroyAndRebuildIndex(ElasticsearchTestingHelper.ES_INDEX_DOCUMENTS);

        // building a fake environment
        gedLibrary = new EdmLibrary();
        gedLibrary.setName("library");
        gedLibrary = gedLibraryService.save(gedLibrary);

        libraryId = gedLibrary.getId();

        gedDirectory = new EdmDirectory();
        gedDirectory.setName("directory");
        gedDirectory.setParentId(gedLibrary.getId());
        gedDirectory = gedDirectoryService.save(gedDirectory);

        directoryId = gedDirectory.getId();

        gedDocument = new EdmDocument();
        gedDocument.setName("document");
        gedDocument.setParentId(gedDirectory.getId());
        gedDocument = gedDocumentService.save(gedDocument);

        documentId = gedDocument.getId();

        directoryWithDirectoryParent = new EdmDirectory();
        directoryWithDirectoryParent.setName("subdirectory");
        directoryWithDirectoryParent.setParentId(gedDirectory.getId());
        directoryWithDirectoryParent = gedDirectoryService.save(directoryWithDirectoryParent);

        directoryWithDirectoryParentId = directoryWithDirectoryParent.getId();

        documentUnderLibrary = new EdmDocument();
        documentUnderLibrary.setName("document under library");
        documentUnderLibrary.setParentId(gedLibrary.getId());
        documentUnderLibrary = gedDocumentService.save(documentUnderLibrary);

        documentUnderLibraryId = documentUnderLibrary.getId();

        elasticsearchTestingHelper.flushIndex(ElasticsearchTestingHelper.ES_INDEX_DOCUMENTS);
    }

    @Test
    public void libraryNodeShouldBeReturned() {
        EdmNode node = gedNodeService.findOne(libraryId);
        assertThat(node).isNotNull();
        assertThat(node.getId()).isEqualTo(libraryId);
        assertThat(node.getGedNodeType()).isEqualTo(EdmNodeType.LIBRARY);
    }

    @Test
    public void directoryNodeShouldBeReturned() {
        EdmNode node = gedNodeService.findOne(directoryId);
        assertThat(node).isNotNull();
        assertThat(node.getId()).isEqualTo(directoryId);
        assertThat(node.getGedNodeType()).isEqualTo(EdmNodeType.DIRECTORY);
    }

    @Test
    public void documentNodeShouldBeReturned() {
        EdmNode node = gedNodeService.findOne(documentId);
        assertThat(node).isNotNull();
        assertThat(node.getId()).isEqualTo(documentId);
        assertThat(node.getGedNodeType()).isEqualTo(EdmNodeType.DOCUMENT);
    }

    @Test
    public void libraryShouldNotHaveParent() {
        EdmNode node = gedNodeService.findOne(libraryId);
        assertThat(node.getParentId()).isNull();
    }

    @Test
    public void directoryCanHaveLibrayForParent() {
        EdmNode node = gedNodeService.findOne(directoryId);
        assertThat(node.getParentId()).isEqualTo(libraryId);
    }

    @Test
    public void directoryCanHaveDirectoryForParent() {
        EdmNode node = gedNodeService.findOne(directoryWithDirectoryParentId);
        assertThat(node.getParentId()).isEqualTo(directoryId);
    }

    @Test
    public void documentCanHaveLibraryForParent() {
        EdmNode node = gedNodeService.findOne(documentUnderLibraryId);
        assertThat(node.getParentId()).isEqualTo(libraryId);
    }

    @Test
    public void documentCanHaveDirectoryForParent() {
        EdmNode node = gedNodeService.findOne(documentId);
        assertThat(node.getParentId()).isEqualTo(directoryId);
    }

    @Test
    public void libraryPathShouldBeLibraryName() {
        EdmNode node = gedNodeService.findOne(libraryId);
        String nodePath = gedNodeService.getPathOfNode(node);
        assertThat(nodePath).isEqualTo("library");
    }

    @Test
    public void directoryPathShouldIncludeLibraryPath() {
        EdmNode node = gedNodeService.findOne(directoryId);
        String nodePath = gedNodeService.getPathOfNode(node);
        assertThat(nodePath).isEqualTo("library/directory");
    }

    @Test
    public void documentPathShouldIncludeDirectoryPath() {
        EdmNode node = gedNodeService.findOne(documentId);
        String nodePath = gedNodeService.getPathOfNode(node);
        assertThat(nodePath).isEqualTo("library/directory/document");
    }

    @Test
    public void libraryHasExpectedChildren() {
        List<EdmNode> nodes = gedNodeService.getChildren(libraryId);

        List<EdmNode> attemptedResult = Arrays.asList(new EdmNode[] { gedDirectory, documentUnderLibrary });

        assertThat(nodes).isNotNull();
        assertThat(nodes.size()).isEqualTo(attemptedResult.size());
        assertThat(nodes).containsAll(attemptedResult);
    }

    @Test
    public void directoryHasExpectedChildren() {
        List<EdmNode> nodes = gedNodeService.getChildren(directoryId);

        List<EdmNode> attemptedResult = Arrays.asList(new EdmNode[] { directoryWithDirectoryParent, gedDocument });

        assertThat(nodes).isNotNull();
        assertThat(nodes.size()).isEqualTo(attemptedResult.size());
        assertThat(nodes).containsAll(attemptedResult);
    }

    @Test
    public void anotherDirectoryHasExpectedChildren() {
        List<EdmNode> nodes = gedNodeService.getChildren(directoryWithDirectoryParentId);

        List<EdmNode> attemptedResult = Arrays.asList(new EdmNode[] {});

        assertThat(nodes).isNotNull();
        assertThat(nodes.size()).isEqualTo(attemptedResult.size());
        assertThat(nodes).containsAll(attemptedResult);
    }

    @Test
    public void documentShouldNotHaveChildren() {
        List<EdmNode> nodes = gedNodeService.getChildren(documentId);

        List<EdmNode> attemptedResult = Arrays.asList(new EdmNode[] {});

        assertThat(nodes).isNotNull();
        assertThat(nodes.size()).isEqualTo(attemptedResult.size());
        assertThat(nodes).containsAll(attemptedResult);
    }

    @Test
    public void libraryShouldBeFindByPath() {
        EdmNode node = gedNodeService.findOneByPath("library");
        
        assertThat(node).isNotNull();
        assertThat(node).isEqualTo(gedLibrary);
    }
    
    @Test
    public void directoryShouldBeFindByPath() {
        EdmNode node = gedNodeService.findOneByPath("library/directory");
        
        assertThat(node).isNotNull();
        assertThat(node).isEqualTo(gedDirectory);
    }
    
    @Test
    public void documentShouldBeFindByPath() {
        EdmNode node = gedNodeService.findOneByPath("library/directory/document");
        
        assertThat(node).isNotNull();
        assertThat(node).isEqualTo(gedDocument);
    }
    
    @Test
    public void subdirectoryShouldBeFindByPath() {
        EdmNode node = gedNodeService.findOneByPath("library/directory/subdirectory");
        
        assertThat(node).isNotNull();
        assertThat(node).isEqualTo(directoryWithDirectoryParent);
    }
}
