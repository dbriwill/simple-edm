package fr.simple.ged.service;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import fr.simple.ged.Application;
import fr.simple.ged.ElasticsearchTestingHelper;
import fr.simple.ged.common.GedNodeType;
import fr.simple.ged.model.GedDirectory;
import fr.simple.ged.model.GedDocument;
import fr.simple.ged.model.GedFile;
import fr.simple.ged.model.GedLibrary;
import fr.simple.ged.model.GedNode;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { Application.class })
@ComponentScan(basePackages = { "fr.simple.ged" })
public class GedNodeServiceTest {

	@Autowired
	private GedNodeService gedNodeService;
	
	@Autowired
	private GedLibraryService gedLibraryService;
	
	@Autowired
	private GedDocumentService gedDocumentService;
	
	@Autowired
	private GedDirectoryService gedDirectoryService;

	@Autowired
	private ElasticsearchTestingHelper elasticsearchTestingHelper;
	
	
	// testing id's
	private String libraryId;
	private String directoryId;
	private String documentId;

	private String directoryWithDirectoryParentId;
	private String documentWithLibraryParentId;
	
	
	@Before
	public void setUp() throws Exception {
		elasticsearchTestingHelper.destroyAndRebuildIndex(ElasticsearchTestingHelper.ES_INDEX_DOCUMENTS);
		
		// building a fake environment
		GedLibrary gedLibrary = new GedLibrary();
		gedLibrary.setName("library");
		gedLibrary = gedLibraryService.save(gedLibrary);
		
		libraryId = gedLibrary.getId();
		
		GedDirectory gedDirectory = new GedDirectory();
		gedDirectory.setName("directory");
		gedDirectory.setParentId(gedLibrary.getId());
		gedDirectory = gedDirectoryService.save(gedDirectory);
		
		directoryId = gedDirectory.getId();
		
		GedDocument gedDocument = new GedDocument();
		gedDocument.setName("document");
		gedDocument.setParentId(gedDirectory.getId());
		gedDocument = gedDocumentService.save(gedDocument);
		
		documentId = gedDocument.getId();
		
		GedFile gedFile = new GedFile();
		gedFile.setName("file");
		
		GedDirectory anotherGedDirectory = new GedDirectory();
		anotherGedDirectory.setName("Another ged directory");
		anotherGedDirectory.setParentId(gedDirectory.getId());
		anotherGedDirectory = gedDirectoryService.save(anotherGedDirectory);
		
		directoryWithDirectoryParentId = anotherGedDirectory.getId();
		
		GedDocument documentUnderLibrary = new GedDocument();
		documentUnderLibrary.setName("document under library");
		documentUnderLibrary.setParentId(gedLibrary.getId());
		documentUnderLibrary = gedDocumentService.save(documentUnderLibrary);
		
		documentWithLibraryParentId = documentUnderLibrary.getId();
	}
	
	@Test
	public void libraryNodeShouldBeReturned() {
		GedNode node = gedNodeService.findOne(libraryId);
		assertThat(node).isNotNull();
		assertThat(node.getId()).isEqualTo(libraryId);
		assertThat(node.getGedNodeType()).isEqualTo(GedNodeType.LIBRARY);
	}
	
	@Test
	public void directoryNodeShouldBeReturned() {
		GedNode node = gedNodeService.findOne(directoryId);
		assertThat(node).isNotNull();
		assertThat(node.getId()).isEqualTo(directoryId);
		assertThat(node.getGedNodeType()).isEqualTo(GedNodeType.DIRECTORY);
	}
	
	@Test
	public void documentNodeShouldBeReturned() {
		GedNode node = gedNodeService.findOne(documentId);
		assertThat(node).isNotNull();
		assertThat(node.getId()).isEqualTo(documentId);
		assertThat(node.getGedNodeType()).isEqualTo(GedNodeType.DOCUMENT);
	}
	
	@Test
	public void libraryShouldNotHaveParent() {
		GedNode node = gedNodeService.findOne(libraryId);
		assertThat(node.getParentId()).isNull();
	}
	
	@Test
	public void directoryCanHaveLibrayForParent() {
		GedNode node = gedNodeService.findOne(directoryId);
		assertThat(node.getParentId()).isEqualTo(libraryId);
	}
	
	@Test
	public void directoryCanHaveDirectoryForParent() {
		GedNode node = gedNodeService.findOne(directoryWithDirectoryParentId);
		assertThat(node.getParentId()).isEqualTo(directoryId);
	}
	
	@Test
	public void documentCanHaveLibraryForParent() {
		GedNode node = gedNodeService.findOne(documentWithLibraryParentId);
		assertThat(node.getParentId()).isEqualTo(libraryId);
	}
	
	@Test
	public void documentCanHaveDirectoryForParent() {
		GedNode node = gedNodeService.findOne(documentId);
		assertThat(node.getParentId()).isEqualTo(directoryId);
	}
	
	@Test
	public void libraryPathShouldBeLibraryName() {
		GedNode node = gedNodeService.findOne(libraryId);
		String nodePath = gedNodeService.getPathOfNode(node);
		assertThat(nodePath).isEqualTo("library");
	}
	
	@Test
	public void directoryPathShouldIncludeLibraryPath() {
		GedNode node = gedNodeService.findOne(directoryId);
		String nodePath = gedNodeService.getPathOfNode(node);
		assertThat(nodePath).isEqualTo("library/directory");
	}
	
	@Test
	public void documentPathShouldIncludeDirectoryPath() {
		GedNode node = gedNodeService.findOne(documentId);
		String nodePath = gedNodeService.getPathOfNode(node);
		assertThat(nodePath).isEqualTo("library/directory/document");
	}
}
