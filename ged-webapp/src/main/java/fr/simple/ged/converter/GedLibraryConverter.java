package fr.simple.ged.converter;

import javax.inject.Named;

import fr.simple.ged.common.dto.GedLibraryDto;
import fr.simple.ged.model.GedLibrary;

// TODO : make a abstract converter
@Named
public class GedLibraryConverter {

	public GedLibraryDto modelToDto(GedLibrary model) {
		GedLibraryDto dto = new GedLibraryDto();
		dto.setDescription(model.getDescription());
		dto.setId(model.getId());
		dto.setName(model.getName());
		return dto;
	}
	
	public GedLibrary dtoToModel(GedLibraryDto dto) {
		GedLibrary library = new GedLibrary();
		library.setDescription(dto.getDescription());
		library.setId(dto.getId());
		library.setName(dto.getName());
		return library;
	}
	
}
