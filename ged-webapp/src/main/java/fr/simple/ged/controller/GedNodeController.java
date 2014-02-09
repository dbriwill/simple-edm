package fr.simple.ged.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.simple.ged.common.dto.GedNodeDto;
import fr.simple.ged.mapper.GedNodeMapper;
import fr.simple.ged.service.GedNodeService;

@RestController
public class GedNodeController {
	
    private final Logger logger = LoggerFactory.getLogger(GedNodeController.class);
    
	@Inject
	private GedNodeService gedNodeService;
	
	@Inject
	private GedNodeMapper gedNodeMapper;
	
	// not really restfull
	@RequestMapping(value = "/node/{nodepath}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody GedNodeDto read(@PathVariable String nodepath) {
	    logger.debug("get node for path : '{}'", nodepath);
        return gedNodeMapper.boToDto(gedNodeService.findOneByPath(nodepath));
    }
	
	// not really restfull
	@RequestMapping(value = "/node/childs/{nodeid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<GedNodeDto> getChildNodes(@PathVariable String nodeid) {
		return gedNodeMapper.boToDto(gedNodeService.getChildren(nodeid));
	}
	
}
