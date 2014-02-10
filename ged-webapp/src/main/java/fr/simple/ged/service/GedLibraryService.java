package fr.simple.ged.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import fr.simple.ged.model.GedDirectory;
import fr.simple.ged.model.GedDocument;
import fr.simple.ged.model.GedLibrary;
import fr.simple.ged.repository.GedLibraryRepository;

@Service
@PropertySources(value = { @PropertySource("classpath:/properties/default_values.fr_fr.properties") })
public class GedLibraryService {

    private final Logger logger = LoggerFactory.getLogger(GedLibraryService.class);

    @Inject
    private Environment env;

    @Inject
    private GedLibraryRepository gedLibraryRepository;

    @Inject
    private GedDirectoryService gedDirectoryService;

    @Inject
    private GedDocumentService gedDocumentService;

    public GedLibrary findOne(String id) {
        return gedLibraryRepository.findOne(id);
    }

    public List<GedLibrary> getGedLibraries() {
        List<GedLibrary> gedLibraries = new ArrayList<>();
        for (GedLibrary l : gedLibraryRepository.findAll()) {
            gedLibraries.add(l);
        }
        return gedLibraries;
    }

    public void createDefaultLibraryIfNotExists() {
        logger.debug("Checking for at least one library found...");
        if (gedLibraryRepository.count() == 0) {
            logger.info("Creating default library");

            GedLibrary library = new GedLibrary();
            library.setName(env.getProperty("default.library.name"));
            library.setDescription(env.getProperty("default.library.description"));
            library = save(library);

            GedDirectory directory = new GedDirectory();
            directory.setName(env.getProperty("default.directory.name"));
            directory.setParentId(library.getId());
            directory = gedDirectoryService.save(directory);

            GedDocument document = new GedDocument();
            document.setName(env.getProperty("default.document.name"));
            document.setParentId(directory.getId());
            
            File file = new File("./default-edm.pdf");
            if (!file.exists()) {
                InputStream link = (getClass().getResourceAsStream(env.getProperty("default.document.path")));
                try {
                    Files.copy(link, file.getAbsoluteFile().toPath());
                } catch (IOException e) {
                    logger.error("Failed to load default edm file", e);
                }
            }
            
            document.setFilename(file.getPath());
            document = gedDocumentService.save(document);
        }
    }

    public GedLibrary save(GedLibrary gedLibrary) {
        return gedLibraryRepository.index(gedLibrary);
    }

    public List<GedLibrary> findByName(String name) {
        return gedLibraryRepository.findByName(name);
    }
}
