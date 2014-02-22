package fr.simple.edm.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import fr.simple.edm.common.dto.EdmDocumentDto;
import fr.simple.edm.common.dto.EdmDocumentUploadResponse;
import fr.simple.edm.mapper.EdmDocumentMapper;
import fr.simple.edm.model.EdmDocument;
import fr.simple.edm.service.EdmDocumentService;

@RestController
@PropertySources(value = { 
        @PropertySource("classpath:/edm-configuration.properties")
})
public class EdmDocumentController {

    private final Logger logger = LoggerFactory.getLogger(EdmDocumentController.class);

    @Inject
    private Environment env;
    
    @Inject
    private EdmDocumentService edmDocumentService;
    
    @Inject
    private EdmDocumentMapper edmDocumentMapper;

    @RequestMapping(value = "/document/file/**", method = RequestMethod.GET)
    public void getFile(HttpServletRequest request, HttpServletResponse response) {
        String filePath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        filePath = filePath.replaceFirst("/document/file/", "");
        logger.debug("get file for path : '{}'", filePath);
        
        EdmDocument document = edmDocumentService.findEdmDocumentByFilePath(filePath);
        response.setContentType(document.getFileContentType());
        String serverFilePath = edmDocumentService.getServerPathOfEdmDocument(document);
        
        try (InputStream is = new FileInputStream(serverFilePath)) {
            // copy it to response's OutputStream
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            logger.error("Error writing file to output stream. server path was '{}'", serverFilePath);
        }
    }
    
    @RequestMapping(value="/document/upload", method=RequestMethod.POST , headers = "content-type=multipart/*")
    @ResponseStatus(value=HttpStatus.OK)
    public @ResponseBody EdmDocumentUploadResponse execute(@RequestParam(value = "file", required = true) MultipartFile multipartFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String fileExtension = com.google.common.io.Files.getFileExtension(multipartFile.getOriginalFilename());
        String temporaryFileToken =  String.valueOf(System.currentTimeMillis()) + String.valueOf(Math.random() + "." + fileExtension);
        
        File file = new File(env.getProperty("edm.tmpdir") + temporaryFileToken);
        multipartFile.transferTo(file);
        
        EdmDocumentUploadResponse uploadResponse = new EdmDocumentUploadResponse();
        uploadResponse.setTemporaryFileToken(temporaryFileToken);
        return uploadResponse;
    }
    
    
    @RequestMapping(method=RequestMethod.POST, value="/document", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody EdmDocumentDto create(@RequestBody EdmDocumentDto edmDocument) {
        EdmDocument document = edmDocumentMapper.dtoToBo(edmDocument);
        if (edmDocument.getTemporaryFileToken() != null) {
            String tmpFileLocation = env.getProperty("edm.tmpdir") + edmDocument.getTemporaryFileToken();
            document.setFilename(tmpFileLocation);
        }
        return edmDocumentMapper.boToDto(edmDocumentService.save(document));
    }
    
}
