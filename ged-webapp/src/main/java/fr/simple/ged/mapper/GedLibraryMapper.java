package fr.simple.ged.mapper;

import javax.inject.Named;

import fr.simple.ged.common.dto.GedLibraryDto;
import fr.simple.ged.model.GedLibrary;

@Named
public class GedLibraryMapper extends AbstractMapper<GedLibrary, GedLibraryDto> {

	public GedLibraryMapper() {
		super(GedLibrary.class, GedLibraryDto.class);
	}

}
