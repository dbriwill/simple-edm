package fr.simple.edm.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import fr.simple.edm.service.EdmDocumentService;

@RestController
public class EdmDocumentController {

    private final Logger logger = LoggerFactory.getLogger(EdmDocumentController.class);

    @Inject
    private EdmDocumentService edmDocumentService;

    @RequestMapping(value = "/document/file/**", method = RequestMethod.GET)
    public void getFile(HttpServletRequest request, HttpServletResponse response) {
        String filePath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        filePath = filePath.replaceFirst("/document/file/", "");
        logger.debug("get file for path : '{}'", filePath);
        
        String serverFilePath = edmDocumentService.getServerPathOfFile(filePath);
        
        try {
            // get your file as InputStream
            InputStream is = new FileInputStream(serverFilePath);
            // copy it to response's OutputStream
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            logger.error("Error writing file to output stream. server path was '{}'", serverFilePath);
        }
    }
}
