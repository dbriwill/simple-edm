package fr.simple.edm.mapper;

import javax.inject.Named;

import fr.simple.edm.common.dto.EdmDirectoryDto;
import fr.simple.edm.model.EdmDirectory;

@Named
public class EdmDirectoryMapper extends AbstractMapper<EdmDirectory, EdmDirectoryDto> {

	public EdmDirectoryMapper() {
		super(EdmDirectory.class, EdmDirectoryDto.class);
	}

}
