package fr.simple.edm.mapper;

import javax.inject.Inject;
import javax.inject.Named;

import fr.simple.edm.common.dto.EdmDirectoryDto;
import fr.simple.edm.model.EdmDirectory;
import fr.simple.edm.service.EdmNodeService;

@Named
public class EdmDirectoryMapper extends AbstractMapper<EdmDirectory, EdmDirectoryDto> {

    @Inject 
    private EdmNodeService edmNodeService;
    
	public EdmDirectoryMapper() {
		super(EdmDirectory.class, EdmDirectoryDto.class);
	}
	
    @Override
    public EdmDirectoryDto boToDto(EdmDirectory bo) {
        EdmDirectoryDto dto = super.boToDto(bo);
        // additional fields
        if (dto.getId() != null && ! dto.getId().isEmpty()) {
            dto.setNodePath(edmNodeService.getPathOfNode(bo));
        }
        return dto;
    }

}
