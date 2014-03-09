package fr.simple.edm.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import fr.simple.edm.model.EdmDirectory;
import fr.simple.edm.model.EdmDocument;
import fr.simple.edm.model.EdmLibrary;
import fr.simple.edm.repository.EdmLibraryRepository;

@Service
@PropertySources(value = { 
        @PropertySource("classpath:/edm-configuration.properties")
})
public class EdmLibraryService {

    private final Logger logger = LoggerFactory.getLogger(EdmLibraryService.class);

    @Inject
    private MessageSource messageSource;
    
    @Inject
    private Environment env;

    @Inject
    private EdmLibraryRepository edmLibraryRepository;

    @Inject
    private EdmDirectoryService edmDirectoryService;

    @Inject
    private EdmDocumentService edmDocumentService;

    public EdmLibrary findOne(String id) {
        return edmLibraryRepository.findOne(id);
    }

    public List<EdmLibrary> getEdmLibraries() {
        List<EdmLibrary> edmLibraries = new ArrayList<>();
        for (EdmLibrary l : edmLibraryRepository.findAll()) {
            edmLibraries.add(l);
        }
        return edmLibraries;
    }

    public void createDefaultLibraryIfNotExists() {
        logger.debug("Checking for at least one library found...");
        if (edmLibraryRepository.count() == 0) {
            logger.info("Creating default library");

            EdmLibrary library = new EdmLibrary();
            library.setName(messageSource.getMessage("default.library.name", new Object[] {}, "Library name", Locale.getDefault()));
            library.setDescription(messageSource.getMessage("default.library.description", new Object[] {}, "Library description", Locale.getDefault()));
            library = save(library);

            EdmDirectory directory = new EdmDirectory();
            directory.setName(messageSource.getMessage("default.directory.name", new Object[] {}, "Directory name", Locale.getDefault()));
            directory.setParentId(library.getId());
            directory = edmDirectoryService.save(directory);

            EdmDocument document = new EdmDocument();
            document.setName(messageSource.getMessage("default.document.name", new Object[] {}, "Document name", Locale.getDefault()));
            document.setParentId(directory.getId());
            document.setDescription(messageSource.getMessage("default.document.description", new Object[] {}, "Document description", Locale.getDefault()));
            document.setDate(new Date());
            
            File file = new File(env.getProperty("edm.tmpdir") + "default-edm.pdf");
            
            if (!file.exists()) {
                logger.info("Adding '{}' in '{}'", messageSource.getMessage("default.document.path", new Object[] {}, "", Locale.getDefault()), file.getAbsoluteFile());
                InputStream link = (getClass().getResourceAsStream(messageSource.getMessage("default.document.path", new Object[] {}, "", Locale.getDefault())));
                try {
                    com.google.common.io.Files.createParentDirs(file);
                    Files.copy(link, file.getAbsoluteFile().toPath());
                } catch (Exception e) {
                    logger.error("Failed to export default edm file", e);
                }
            }
            
            document.setFilename(file.getPath());
            document = edmDocumentService.save(document);
        }
    }

    public EdmLibrary save(EdmLibrary edmLibrary) {
        return edmLibraryRepository.index(edmLibrary);
    }

    public List<EdmLibrary> findByName(String name) {
        return edmLibraryRepository.findByName(name);
    }
    
    public void delete(EdmLibrary edmLibrary) {
        edmLibraryRepository.delete(edmLibrary);
    }
}
