package fr.simple.edm.mapper;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.ObjectUtils;

import fr.simple.edm.common.dto.EdmNodeDto;
import fr.simple.edm.model.EdmNode;
import fr.simple.edm.service.EdmNodeService;

/**
 * Mapper is override because I wan't to keep all json properties (children customed properties), not only EdmProperty
 */
@Named
public class EdmNodeMapper extends AbstractMapper<EdmNode, EdmNodeDto> {

    @Inject
    private EdmLibraryMapper edmLibraryMapper;

    @Inject
    private EdmDirectoryMapper edmDirectoryMapper;
    
    @Inject
    private EdmDocumentMapper edmDocumentMapper;
    
    @Inject 
    private EdmNodeService edmNodeService;
    
    public EdmNodeMapper() {
        super(EdmNode.class, EdmNodeDto.class);
    }

    @Override
    public EdmNode dtoToBo(EdmNodeDto dto) {
        return ObjectUtils.firstNonNull(
                edmLibraryMapper.dtoToBoOrNull(dto),
                edmDirectoryMapper.dtoToBoOrNull(dto),
                edmDocumentMapper.dtoToBoOrNull(dto),
                new EdmNode()
        );
    }

    @Override
    public EdmNodeDto boToDto(EdmNode bo) {
        EdmNodeDto dto = ObjectUtils.firstNonNull(
                edmLibraryMapper.boToDtoOrNull(bo),
                edmDirectoryMapper.boToDtoOrNull(bo),
                edmDocumentMapper.boToDtoOrNull(bo),
                new EdmNodeDto()
        );
        // additional fields
        if (dto.getId() != null && ! dto.getId().isEmpty()) {
            dto.setNodePath(edmNodeService.getPathOfNode(bo));
        }
        return dto;
    }
}
