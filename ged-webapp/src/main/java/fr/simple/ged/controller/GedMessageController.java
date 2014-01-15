package fr.simple.ged.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.simple.ged.common.dto.GedMessageDto;

@Controller
public class GedMessageController {
	
	private static final Logger logger = LoggerFactory.getLogger(GedMessageController.class);
	
	
    @RequestMapping(value = "/messages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<GedMessageDto> getMessages() {
        List<GedMessageDto> gedMessages = new ArrayList<>();
    	
        gedMessages.add(new GedMessageDto("Hello, I'm the first message"));
        gedMessages.add(new GedMessageDto("Hello, I'm another message"));
        
        return gedMessages;
    }
    
    
    @RequestMapping(value = "/message/{messageid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody GedMessageDto getMessage(@PathVariable String messageid) {
        return new GedMessageDto("Hello, I'm the message you requested (number " + messageid + ")");
    }
    
    
    
    @RequestMapping(value = "/message", method = {RequestMethod.POST, RequestMethod.PUT}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody GedMessageDto postMessage() {
        GedMessageDto gedMessage = new GedMessageDto("plop");
        return gedMessage;
    }
    
    

//    public static void saveGedMessage(GedMessageDto doc) {
//
//        try {
//                
//                // the document is build manualy to 
//                // have the possibility to add the binary file
//                // content
//                
//                XContentBuilder contentBuilder = jsonBuilder();
//                
//                // add ged document attributes
//                contentBuilder.startObject();
//                
//                for (Method  m : GedDocument.class.getDeclaredMethods()) {
//                        //contentBuilder = contentBuilder.field(f.getName(), f.get(doc));
//                        if (m.getName().startsWith("get")) {
//                                if (m.getName().equalsIgnoreCase("getDocumentFiles")) { // ignore this type
//                                        continue;
//                                }
//                                Object oo = m.invoke(doc);
//                                contentBuilder.field(m.getName().substring(3), oo);
//                        }
//                }
//                
//                // now add the binaries files
//                
//                if (doc.getDocumentFiles().size() > 0) {
//                        
//                        contentBuilder.startArray("files");
//                        
//                        for (GedDocumentFile gedDocumentFile : doc.getDocumentFiles()) {
//
//                    logger.debug("Adding file '{}{}' for ES indexation", Profile.getInstance().getLibraryRoot(), gedDocumentFile.getRelativeFilePath());
//
//                    contentBuilder.startObject();
//
//                                Path filePath = Paths.get(Profile.getInstance().getLibraryRoot() + gedDocumentFile.getRelativeFilePath());
//                                
//                                String contentType = Files.probeContentType(filePath);
//                                String name        = gedDocumentFile.getRelativeFilePath();
//                                String content     = Base64.encodeBytes(Files.readAllBytes(filePath));
//                                
//                                contentBuilder.field("_content_type", contentType).field("_name", name).field("content", content);
//
//                    contentBuilder.endObject();
//                        }
//
//                contentBuilder.endArray();
//                }
//
//                // and that's all folks
//                contentBuilder.endObject();
//
//            IndexResponse ir = node.client().prepareIndex(ES_GED_INDEX, ES_GED_INDEX_TYPE_DOC, Integer.toString(doc.getId())).setSource(contentBuilder).execute().actionGet();
//
//            logger.debug("Indexed ged document {} with id {}", doc.getId(), ir.getId());
//        }
//        catch (Exception e) {
//            logger.error("Failed to index document", e);
//        }
//
//    }

	
}
