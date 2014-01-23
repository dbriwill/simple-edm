package fr.simple.ged.service;

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

import fr.simple.ged.Application;
import fr.simple.ged.ElasticsearchTestingHelper;
import fr.simple.ged.model.GedDocument;
import fr.simple.ged.model.GedFile;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { Application.class })
@ComponentScan(basePackages = { "fr.simple.ged" })
public class GedDocumentServiceTest {

	@Autowired
	private ElasticsearchTestingHelper elasticsearchTestingHelper;
	
	@Autowired
	private GedDocumentService gedDocumentService;
	
	
    private GedDocument docBac;
    private GedDocument docBrevet;
    private GedDocument docBacNotes;
    private GedDocument docLatex;
	
    
	/**
	 * Will destroy and rebuild ES_INDEX
	 */
	@Before
	public void setUp() throws Exception {
		elasticsearchTestingHelper.destroyAndRebuildIndex(ElasticsearchTestingHelper.ES_INDEX_DOCUMENTS);
		
        docBac = new GedDocument();
        docBac.setName("Diplome du bac");
        docBac.setDescription("Bla");

        docBrevet = new GedDocument();
        docBrevet.setName("Brevet");
        docBrevet.setDescription("diplôme du brevet");

        docBacNotes = new GedDocument();
        docBacNotes.setName("Notes du bac");
        docBacNotes.setDescription("Relevé de notes du bac");

        docLatex = new GedDocument();
        docLatex.setName("Un template de document...");
        docLatex.setDescription("...réalisé dans un format binaire");
        //docLatex.addFile(new GedFile("demo_pdf.pdf"));
        
        gedDocumentService.save(docBac);
        gedDocumentService.save(docBrevet);
        gedDocumentService.save(docBacNotes);
        gedDocumentService.save(docLatex);
        
        elasticsearchTestingHelper.flushIndex(ElasticsearchTestingHelper.ES_INDEX_DOCUMENTS);
	}
	
    /**
     * Search on doc name, very basic
     */
    @Test
    public void searchOnDocName() throws Exception {
        List<GedDocument> docs = gedDocumentService.search("brevet");

        List<GedDocument> attemptedResult = Arrays.asList(new GedDocument[]{
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
    public void searchDocumentWithAccentTest() throws Exception {
        List<GedDocument> docs = gedDocumentService.search("diplôme");

        List<GedDocument> attemptedResult = Arrays.asList(new GedDocument[]{
                docBac, docBrevet
        });

        assertThat(docs).isNotNull();
        assertThat(docs.size()).isEqualTo(attemptedResult.size());
        assertThat(docs).containsAll(attemptedResult);
    }

    /**
     * Search test with ending 's'
     */
    @Test
    public void searchDocumentWithSTest() throws Exception {
        List<GedDocument> docs = gedDocumentService.search("diplômes");

        List<GedDocument> attemptedResult = Arrays.asList(new GedDocument[]{
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
    public void searchMultiWordDocumentTest() throws Exception {
        List<GedDocument> docs = gedDocumentService.search("diplôme bac");

        List<GedDocument> attemptedResult = Arrays.asList(new GedDocument[]{
                docBac
        });
        
        assertThat(docs).isNotNull();
        assertThat(docs.size()).isEqualTo(attemptedResult.size());
        assertThat(docs).containsAll(attemptedResult);
    }


//    /**
//     * Search in document binary content
//     */
//    @Test
//    public void searchOnBinaryContent() throws Exception {
//            List<GedDocument> docs = gedDocumentService.search("latex");
//
//        List<GedDocument> attemptedResult = Arrays.asList(new GedDocument[]{
//                docLatex
//        });
//
//        assertThat(docs).isNotNull();
//        assertThat(docs.size()).isEqualTo(attemptedResult.size());
//        assertThat(docs).containsAll(attemptedResult);
//    }
//
//    /**
//     * Search in document binary metadata (author)
//     */
//    @Test
//    public void searchOnBinaryMetadataContent() throws Exception {
//        List<GedDocument> docs = gedDocumentService.search("xavier");
//
//        List<GedDocument> attemptedResult = Arrays.asList(new GedDocument[]{
//                docLatex
//        });
//
//        assertThat(docs).isNotNull();
//        assertThat(docs.size()).isEqualTo(attemptedResult.size());
//        assertThat(docs).containsAll(attemptedResult);
//    }

}
