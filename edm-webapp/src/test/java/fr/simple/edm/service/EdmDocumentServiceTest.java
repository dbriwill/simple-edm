package fr.simple.edm.service;

import static org.fest.assertions.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;
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
import fr.simple.edm.model.EdmDocument;
import fr.simple.edm.service.EdmDocumentService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { Application.class })
@ComponentScan(basePackages = { "fr.simple.edm" })
public class EdmDocumentServiceTest {

	@Autowired
	private ElasticsearchTestingHelper elasticsearchTestingHelper;
	
	@Autowired
	private EdmNodeService edmNodeService;
	
	@Autowired
	private EdmDocumentService edmDocumentService;
	
	@Autowired
	private EdmLibraryService edmLibraryService;
	
	
    private EdmDocument docBac;
    private EdmDocument docBrevet;
    private EdmDocument docBacNotes;
    private EdmDocument docLatex;
	
    
	/**
	 * Will destroy and rebuild ES_INDEX
	 */
	@Before
	public void setUp() throws Exception {
		elasticsearchTestingHelper.destroyAndRebuildIndex(ElasticsearchTestingHelper.ES_INDEX_DOCUMENTS);
		
		String targetDirAbsolutePath = System.getProperty("user.dir") + (System.getProperty("user.dir").contains("edm-webapp") ? "" : "/edm-webapp") + "/target/test-classes/";
		
		edmLibraryService.createDefaultLibraryIfNotExists();
		
		
        docBac = new EdmDocument();
        docBac.setName("Diplome du bac");
        docBac.setDescription("Bla");

        docBrevet = new EdmDocument();
        docBrevet.setName("Brevet");
        docBrevet.setDescription("diplôme du brevet");

        docBacNotes = new EdmDocument();
        docBacNotes.setName("Notes du bac");
        docBacNotes.setDescription("Relevé de notes du bac");

        docLatex = new EdmDocument();
        docLatex.setName("Un template de document");
        docLatex.setDescription("réalisé dans un format binaire");
        // make a copy because moving test file is not acceptable (someone may come after and require this file) ! 
        Files.copy(Paths.get(targetDirAbsolutePath + "demo_pdf.pdf"), Paths.get(targetDirAbsolutePath + "demo_pdf_tmp.pdf"));
        docLatex.setFilename(targetDirAbsolutePath + "demo_pdf_tmp.pdf");
        
        edmDocumentService.save(docBac);
        edmDocumentService.save(docBrevet);
        edmDocumentService.save(docBacNotes);
        edmDocumentService.save(docLatex);
        
        elasticsearchTestingHelper.flushIndex(ElasticsearchTestingHelper.ES_INDEX_DOCUMENTS);
	}

	
    /**
     * Search on doc name, very basic
     */
    @Test
    public void documentWhichContainsWordShouldBeReturned() throws Exception {
        List<EdmDocument> docs = edmDocumentService.search("brevet");

        List<EdmDocument> attemptedResult = Arrays.asList(new EdmDocument[]{
        		docBrevet
        });
        
        assertThat(docs).isNotNull();
        assertThat(docs.size()).isEqualTo(attemptedResult.size());
        assertThat(docs).containsAll(attemptedResult);
    }
	
    /**
     * Search test with accented word
     */
    @Test
    public void documentWithAccentShouldBeReturned() throws Exception {
        List<EdmDocument> docs = edmDocumentService.search("brevets");

        List<EdmDocument> attemptedResult = Arrays.asList(new EdmDocument[]{
                docBrevet
        });

        assertThat(docs).isNotNull();
        assertThat(docs.size()).isEqualTo(attemptedResult.size());
        assertThat(docs).containsAll(attemptedResult);
    }


    /**
     * Search test with ending 's'
     */
    @Test
    public void documentWithSShouldBeReturned() throws Exception {
        List<EdmDocument> docs = edmDocumentService.search("diplômes");

        List<EdmDocument> attemptedResult = Arrays.asList(new EdmDocument[]{
                docBac, docBrevet
        });

        assertThat(docs).isNotNull();
        assertThat(docs.size()).isEqualTo(attemptedResult.size());
        assertThat(docs).containsAll(attemptedResult);
    }
    
    /**
     * Search test with ending 's' and accent
     */
    @Test
    public void documentWithAccetAndSShouldBeReturned() throws Exception {
        List<EdmDocument> docs = edmDocumentService.search("diplomes");

        List<EdmDocument> attemptedResult = Arrays.asList(new EdmDocument[]{
                docBac, docBrevet
        });

        assertThat(docs).isNotNull();
        assertThat(docs.size()).isEqualTo(attemptedResult.size());
        assertThat(docs).containsAll(attemptedResult);
    }

    /**
     * Search on multi word
     */
    @Test
    public void documentWitchContainsAllWordsShouldBeReturned() throws Exception {
        List<EdmDocument> docs = edmDocumentService.search("diplôme bac");

        List<EdmDocument> attemptedResult = Arrays.asList(new EdmDocument[]{
                docBac
        });

        assertThat(docs).isNotNull();
        assertThat(docs.size()).isEqualTo(attemptedResult.size());
        assertThat(docs).containsAll(attemptedResult);
    }


    /**
     * Search in document binary content
     */
    @Test
    public void searchOnBinaryContent() throws Exception {
            List<EdmDocument> docs = edmDocumentService.search("latex");

        List<EdmDocument> attemptedResult = Arrays.asList(new EdmDocument[]{
                docLatex
        });

        assertThat(docs).isNotNull();
        assertThat(docs.size()).isEqualTo(attemptedResult.size());
        assertThat(docs).containsAll(attemptedResult);
    }

    /**
     * Search in document binary metadata (author)
     */
    @Test
    public void searchOnBinaryMetadataContent() throws Exception {
        List<EdmDocument> docs = edmDocumentService.search("xavier");

        List<EdmDocument> attemptedResult = Arrays.asList(new EdmDocument[]{
                docLatex
        });

        assertThat(docs).isNotNull();
        assertThat(docs.size()).isEqualTo(attemptedResult.size());
        assertThat(docs).containsAll(attemptedResult);
    }

    @Test
    public void fileShouldFindHisDocumentPath() {
        String filePath = "Documents/Bienvenue/GED.pdf";
        String nodePath = edmDocumentService.filePathToNodePath(filePath);
        
        assertThat(nodePath).isNotNull();
        assertThat(nodePath).isNotEmpty();
        assertThat(nodePath).isEqualTo("Documents/Bienvenue/GED");
    }
    
}
