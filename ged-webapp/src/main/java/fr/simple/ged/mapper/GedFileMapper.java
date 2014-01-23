package fr.simple.ged.mapper;

import fr.simple.ged.common.dto.GedFileDto;
import fr.simple.ged.model.GedFile;

public class GedFileMapper extends AbstractMapper<GedFile, GedFileDto> {

	public GedFileMapper() {
		super(GedFile.class, GedFileDto.class);
	}

}
