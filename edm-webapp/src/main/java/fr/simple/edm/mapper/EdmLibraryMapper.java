package fr.simple.edm.mapper;

import javax.inject.Named;

import fr.simple.edm.common.dto.EdmLibraryDto;
import fr.simple.edm.model.EdmLibrary;

@Named
public class EdmLibraryMapper extends AbstractMapper<EdmLibrary, EdmLibraryDto> {

	public EdmLibraryMapper() {
		super(EdmLibrary.class, EdmLibraryDto.class);
	}

}
