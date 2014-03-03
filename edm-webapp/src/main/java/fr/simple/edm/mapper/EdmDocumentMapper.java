package fr.simple.edm.mapper;

import javax.inject.Inject;
import javax.inject.Named;

import fr.simple.edm.common.dto.EdmDocumentDto;
import fr.simple.edm.model.EdmDocument;
import fr.simple.edm.service.EdmDocumentService;
import fr.simple.edm.service.EdmNodeService;

@Named
public class EdmDocumentMapper extends AbstractMapper<EdmDocument, EdmDocumentDto> {

    @Inject
    private EdmDocumentService edmDocumentService;
    
    @Inject 
    private EdmNodeService edmNodeService;
    
    public EdmDocumentMapper() {
        super(EdmDocument.class, EdmDocumentDto.class);
    }

    @Override
    public EdmDocumentDto boToDto(EdmDocument bo) {
        EdmDocumentDto dto = super.boToDto(bo);
        // additional fields
        if (dto.getId() != null && ! dto.getId().isEmpty()) {
            dto.setNodePath(edmNodeService.getPathOfNode(bo));
            dto.setServerDocumentFilePath(edmDocumentService.getServerPathOfEdmDocument(bo));
        }
        return dto;
    }

}
