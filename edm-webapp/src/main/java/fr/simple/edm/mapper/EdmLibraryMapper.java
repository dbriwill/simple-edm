package fr.simple.edm.mapper;

import javax.inject.Inject;
import javax.inject.Named;

import fr.simple.edm.common.dto.EdmLibraryDto;
import fr.simple.edm.model.EdmLibrary;
import fr.simple.edm.service.EdmNodeService;

@Named
public class EdmLibraryMapper extends AbstractMapper<EdmLibrary, EdmLibraryDto> {

    @Inject 
    private EdmNodeService edmNodeService;
    
	public EdmLibraryMapper() {
		super(EdmLibrary.class, EdmLibraryDto.class);
	}

    @Override
    public EdmLibraryDto boToDto(EdmLibrary bo) {
        EdmLibraryDto dto = super.boToDto(bo);
        // additional fields
        if (dto.getId() != null && ! dto.getId().isEmpty()) {
            dto.setNodePath(edmNodeService.getPathOfNode(bo));
        }
        return dto;
    }
}
	
