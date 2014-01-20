package fr.simple.ged.mapper;

import javax.inject.Named;

import fr.simple.ged.common.dto.GedDirectoryDto;
import fr.simple.ged.model.GedDirectory;

@Named
public class GedDirectoryMapper extends AbstractMapper<GedDirectory, GedDirectoryDto> {

	public GedDirectoryMapper() {
		super(GedDirectory.class, GedDirectoryDto.class);
	}

}
