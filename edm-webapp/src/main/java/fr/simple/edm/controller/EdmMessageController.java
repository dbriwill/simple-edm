package fr.simple.edm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.simple.edm.common.dto.EdmMessageDto;

@RestController
public class EdmMessageController {
	
	//private static final Logger logger = LoggerFactory.getLogger(GedMessageController.class);

    // a voir : https://github.com/spring-projects/spring-data-elasticsearch
	
    @RequestMapping(value = "/message", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<EdmMessageDto> list() {
        List<EdmMessageDto> edmMessages = new ArrayList<>();

        edmMessages.add(new EdmMessageDto("id_1", "Hello, I'm the first message"));
        edmMessages.add(new EdmMessageDto("id_2", "Hello, I'm another message"));
        
        return edmMessages;
    }

    @RequestMapping(value = "/message/{messageid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody EdmMessageDto read(@PathVariable String messageid) {
        return new EdmMessageDto("id_" + messageid, "Hello, I'm the message you requested");
    }

    @RequestMapping(method=RequestMethod.POST, value="/message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody EdmMessageDto create(@RequestBody EdmMessageDto edmMessage) {
        //employeeDS.add(e);
        return edmMessage;
    }

    @RequestMapping(method=RequestMethod.PUT, value="/message/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody EdmMessageDto update(
            @RequestBody EdmMessageDto edmMessage, @PathVariable String id) {
        //employeeDS.update(e);
        return edmMessage;
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/message/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody void delete(@PathVariable String id) {
        //employeeDS.remove(Long.parseLong(id));
    }

}
