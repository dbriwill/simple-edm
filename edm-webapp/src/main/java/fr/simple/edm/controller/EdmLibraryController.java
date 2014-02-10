package fr.simple.edm.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.simple.edm.common.dto.EdmLibraryDto;
import fr.simple.edm.mapper.EdmLibraryMapper;
import fr.simple.edm.service.EdmLibraryService;

@RestController
public class EdmLibraryController {

	@Inject
	private EdmLibraryService edmLibraryService;
	
	@Inject
	private EdmLibraryMapper edmLibraryMapper;
	
	
    @RequestMapping(value = "/library", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<EdmLibraryDto> list() {
        return edmLibraryMapper.boToDto(edmLibraryService.getEdmLibraries());
    }
	
}
