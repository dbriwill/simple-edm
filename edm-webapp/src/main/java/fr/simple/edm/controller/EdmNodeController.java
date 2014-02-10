package fr.simple.edm.controller;

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

import fr.simple.edm.common.dto.EdmNodeDto;
import fr.simple.edm.mapper.EdmNodeMapper;
import fr.simple.edm.service.EdmNodeService;

@RestController
public class EdmNodeController {
	
    private final Logger logger = LoggerFactory.getLogger(EdmNodeController.class);
    
	@Inject
	private EdmNodeService edmNodeService;
	
	@Inject
	private EdmNodeMapper edmNodeMapper;
	
	// not really restfull
	@RequestMapping(value = "/node/{nodepath}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody EdmNodeDto read(@PathVariable String nodepath) {
	    logger.debug("get node for path : '{}'", nodepath);
        return edmNodeMapper.boToDto(edmNodeService.findOneByPath(nodepath));
    }
	
	// not really restfull
	@RequestMapping(value = "/node/childs/{nodeid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<EdmNodeDto> getChildNodes(@PathVariable String nodeid) {
		return edmNodeMapper.boToDto(edmNodeService.getChildren(nodeid));
	}
	
}
