package fr.simple.ged.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.simple.ged.common.dto.GedLibraryDto;
import fr.simple.ged.service.GedLibraryService;

@RestController
public class GedLibraryController {

	@Inject
	private GedLibraryService gedLibraryService;
	
    @RequestMapping(value = "/library", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<GedLibraryDto> list() {
        return gedLibraryService.getGedLibrariesDto();
    }
	
}
