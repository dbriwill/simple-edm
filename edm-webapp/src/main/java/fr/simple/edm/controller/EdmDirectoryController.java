package fr.simple.edm.controller;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.simple.edm.common.dto.EdmDirectoryDto;
import fr.simple.edm.mapper.EdmDirectoryMapper;
import fr.simple.edm.service.EdmDirectoryService;

@RestController
public class EdmDirectoryController {

	@Inject
	private EdmDirectoryService edmDirectoryService;
	
	@Inject
	private EdmDirectoryMapper edmDirectoryMapper;
	
    @RequestMapping(method=RequestMethod.POST, value="/directory", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody EdmDirectoryDto create(@RequestBody EdmDirectoryDto edmDirectoryDto) {
        return edmDirectoryMapper.boToDto(edmDirectoryService.save(edmDirectoryMapper.dtoToBo(edmDirectoryDto)));
    }
	
}
