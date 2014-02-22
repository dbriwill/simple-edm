package fr.simple.edm.service;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.File;
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
public class EdmNodeServiceTest {

    @Autowired
    private EdmNodeService edmNodeService;

    @Autowired
    private EdmLibraryService edmLibraryService;

    @Autowired
    private EdmDocumentService edmDocumentService;

    @Autowired
    private EdmDirectoryService edmDirectoryService;

    @Autowired
    private ElasticsearchTestingHelper elasticsearchTestingHelper;

    // testing id's
    private EdmLibrary edmLibrary;
    private String libraryId;

    private EdmDirectory edmDirectory;
    private String directoryId;

    private EdmDocument edmDocument;
    private String documentId;

    private EdmDirectory directoryWithDirectoryParent;
    private String directoryWithDirectoryParentId;

    private EdmDocument documentUnderLibrary;
    private String documentUnderLibraryId;

    /*
     * Faked library =============
     * 
     * % gedLibrary 
     *      + gedDirectory 
     *          - gedDocument 
     *          + directoryWithDirectoryParent
     *      - documentUnderLibrary
     */

    @Before
    public void setUp() throws Exception {
        
        String targetDirAbsolutePath = System.getProperty("user.dir") + (System.getProperty("user.dir").contains("edm-webapp") ? "" : "/edm-webapp") + "/target/test-classes/";
        
        elasticsearchTestingHelper.destroyAndRebuildIndex(ElasticsearchTestingHelper.ES_INDEX_DOCUMENTS);

        // building a fake environment
        edmLibrary = new EdmLibrary();
        edmLibrary.setName("library");
        edmLibrary = edmLibraryService.save(edmLibrary);

        libraryId = edmLibrary.getId();

        edmDirectory = new EdmDirectory();
        edmDirectory.setName("directory");
        edmDirectory.setParentId(edmLibrary.getId());
        edmDirectory = edmDirectoryService.save(edmDirectory);

        directoryId = edmDirectory.getId();

        edmDocument = new EdmDocument();
        edmDocument.setName("document");
        edmDocument.setParentId(edmDirectory.getId());
        edmDocument.setFilename(targetDirAbsolutePath + "demo_pdf.pdf");
        edmDocument = edmDocumentService.save(edmDocument);

        documentId = edmDocument.getId();

        directoryWithDirectoryParent = new EdmDirectory();
        directoryWithDirectoryParent.setName("subdirectory");
        directoryWithDirectoryParent.setParentId(edmDirectory.getId());
        directoryWithDirectoryParent = edmDirectoryService.save(directoryWithDirectoryParent);

        directoryWithDirectoryParentId = directoryWithDirectoryParent.getId();

        documentUnderLibrary = new EdmDocument();
        documentUnderLibrary.setName("document under library");
        documentUnderLibrary.setParentId(edmLibrary.getId());
        documentUnderLibrary = edmDocumentService.save(documentUnderLibrary);

        documentUnderLibraryId = documentUnderLibrary.getId();

        elasticsearchTestingHelper.flushIndex(ElasticsearchTestingHelper.ES_INDEX_DOCUMENTS);
    }

    @Test
    public void libraryNodeShouldBeReturned() {
        EdmNode node = edmNodeService.findOne(libraryId);
        assertThat(node).isNotNull();
        assertThat(node.getId()).isEqualTo(libraryId);
        assertThat(node.getEdmNodeType()).isEqualTo(EdmNodeType.LIBRARY);
    }

    @Test
    public void directoryNodeShouldBeReturned() {
        EdmNode node = edmNodeService.findOne(directoryId);
        assertThat(node).isNotNull();
        assertThat(node.getId()).isEqualTo(directoryId);
        assertThat(node.getEdmNodeType()).isEqualTo(EdmNodeType.DIRECTORY);
    }

    @Test
    public void documentNodeShouldBeReturned() {
        EdmNode node = edmNodeService.findOne(documentId);
        assertThat(node).isNotNull();
        assertThat(node.getId()).isEqualTo(documentId);
        assertThat(node.getEdmNodeType()).isEqualTo(EdmNodeType.DOCUMENT);
    }

    @Test
    public void libraryShouldNotHaveParent() {
        EdmNode node = edmNodeService.findOne(libraryId);
        assertThat(node.getParentId()).isNull();
    }

    @Test
    public void directoryCanHaveLibrayForParent() {
        EdmNode node = edmNodeService.findOne(directoryId);
        assertThat(node.getParentId()).isEqualTo(libraryId);
    }

    @Test
    public void directoryCanHaveDirectoryForParent() {
        EdmNode node = edmNodeService.findOne(directoryWithDirectoryParentId);
        assertThat(node.getParentId()).isEqualTo(directoryId);
    }

    @Test
    public void documentCanHaveLibraryForParent() {
        EdmNode node = edmNodeService.findOne(documentUnderLibraryId);
        assertThat(node.getParentId()).isEqualTo(libraryId);
    }

    @Test
    public void documentCanHaveDirectoryForParent() {
        EdmNode node = edmNodeService.findOne(documentId);
        assertThat(node.getParentId()).isEqualTo(directoryId);
    }

    @Test
    public void libraryPathShouldBeLibraryName() {
        EdmNode node = edmNodeService.findOne(libraryId);
        String nodePath = edmNodeService.getPathOfNode(node);
        assertThat(nodePath).isEqualTo("library");
    }

    @Test
    public void directoryPathShouldIncludeLibraryPath() {
        EdmNode node = edmNodeService.findOne(directoryId);
        String nodePath = edmNodeService.getPathOfNode(node);
        assertThat(nodePath).isEqualTo("library/directory");
    }

    @Test
    public void documentPathShouldIncludeDirectoryPath() {
        EdmNode node = edmNodeService.findOne(documentId);
        String nodePath = edmNodeService.getPathOfNode(node);
        assertThat(nodePath).isEqualTo("library/directory/document");
    }

    @Test
    public void libraryHasExpectedChildren() {
        List<EdmNode> nodes = edmNodeService.getChildren(libraryId);

        List<EdmNode> attemptedResult = Arrays.asList(new EdmNode[] { edmDirectory, documentUnderLibrary });

        assertThat(nodes).isNotNull();
        assertThat(nodes.size()).isEqualTo(attemptedResult.size());
        assertThat(nodes).containsAll(attemptedResult);
    }

    @Test
    public void directoryHasExpectedChildren() {
        List<EdmNode> nodes = edmNodeService.getChildren(directoryId);

        List<EdmNode> attemptedResult = Arrays.asList(new EdmNode[] { directoryWithDirectoryParent, edmDocument });

        assertThat(nodes).isNotNull();
        assertThat(nodes.size()).isEqualTo(attemptedResult.size());
        assertThat(nodes).containsAll(attemptedResult);
    }

    @Test
    public void anotherDirectoryHasExpectedChildren() {
        List<EdmNode> nodes = edmNodeService.getChildren(directoryWithDirectoryParentId);

        List<EdmNode> attemptedResult = Arrays.asList(new EdmNode[] {});

        assertThat(nodes).isNotNull();
        assertThat(nodes.size()).isEqualTo(attemptedResult.size());
        assertThat(nodes).containsAll(attemptedResult);
    }

    @Test
    public void documentShouldNotHaveChildren() {
        List<EdmNode> nodes = edmNodeService.getChildren(documentId);

        List<EdmNode> attemptedResult = Arrays.asList(new EdmNode[] {});

        assertThat(nodes).isNotNull();
        assertThat(nodes.size()).isEqualTo(attemptedResult.size());
        assertThat(nodes).containsAll(attemptedResult);
    }

    @Test
    public void libraryShouldBeFindByPath() {
        EdmNode node = edmNodeService.findOneByPath("library");
        
        assertThat(node).isNotNull();
        assertThat(node).isEqualTo(edmLibrary);
    }
    
    @Test
    public void directoryShouldBeFindByPath() {
        EdmNode node = edmNodeService.findOneByPath("library/directory");
        
        assertThat(node).isNotNull();
        assertThat(node).isEqualTo(edmDirectory);
    }
    
    @Test
    public void documentShouldBeFindByPath() {
        EdmNode node = edmNodeService.findOneByPath("library/directory/document");
        
        assertThat(node).isNotNull();
        assertThat(node).isEqualTo(edmDocument);
    }
    
    @Test
    public void subdirectoryShouldBeFindByPath() {
        EdmNode node = edmNodeService.findOneByPath("library/directory/subdirectory");
        
        assertThat(node).isNotNull();
        assertThat(node).isEqualTo(directoryWithDirectoryParent);
    }
    
    @Test
    public void documentCanBeMovedUnderLibrary() {
        File fileBefore = new File(edmNodeService.getServerFilePathOfNode(edmDocument));
        edmDocument.setParentId(libraryId);
        edmDocumentService.save(edmDocument);
        File fileAfter = new File(edmNodeService.getServerFilePathOfNode(edmDocument));
        
        // the file should have a new location
        assertThat(fileBefore.exists()).isFalse();
        assertThat(fileAfter.exists()).isTrue();
    }
    
    @Test
    public void documentCanBeMovedUnderANewDirectory() {
        EdmDirectory newDirectory = new EdmDirectory();
        newDirectory.setName("new directory");
        newDirectory.setParentId(libraryId);
        newDirectory = edmDirectoryService.save(newDirectory);
        
        File fileBefore = new File(edmNodeService.getServerFilePathOfNode(edmDocument));
        edmDocument.setParentId(newDirectory.getId());
        edmDocumentService.save(edmDocument);
        File fileAfter = new File(edmNodeService.getServerFilePathOfNode(edmDocument));
        
        // the file should have a new location
        assertThat(fileBefore.exists()).isFalse();
        assertThat(fileAfter.exists()).isTrue();
    }
    
    @Test
    public void documentCanBeMovedUnderANewDirectoryHierarchy() {
        EdmDirectory newDirectory = new EdmDirectory();
        newDirectory.setName("new directory");
        newDirectory.setParentId(libraryId);
        newDirectory = edmDirectoryService.save(newDirectory);
        
        EdmDirectory newSubDirectory = new EdmDirectory();
        newSubDirectory.setName("new directory");
        newSubDirectory.setParentId(libraryId);
        newSubDirectory = edmDirectoryService.save(newSubDirectory);
        
        File fileBefore = new File(edmNodeService.getServerFilePathOfNode(edmDocument));
        edmDocument.setParentId(newSubDirectory.getId());
        edmDocumentService.save(edmDocument);
        File fileAfter = new File(edmNodeService.getServerFilePathOfNode(edmDocument));
        
        // the file should have a new location
        assertThat(fileBefore.exists()).isFalse();
        assertThat(fileAfter.exists()).isTrue();
    }
}
