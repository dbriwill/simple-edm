package fr.simple.ged.controller;

import java.util.List;

import javax.inject.Inject;

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
	
	@Inject
	private GedNodeService gedNodeService;
	
	@Inject
	private GedNodeMapper gedNodeMapper;
	

	@RequestMapping(value = "/node/{nodeid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody GedNodeDto read(@PathVariable String nodeid) {
        return gedNodeMapper.boToDto(gedNodeService.findOne(nodeid));
    }
	
	// not really restfull
	@RequestMapping(value = "/node/childs/{nodeid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<GedNodeDto> getChildNodes(@PathVariable String nodeid) {
		return gedNodeMapper.boToDto(gedNodeService.getChildren(nodeid));
	}
	
}
