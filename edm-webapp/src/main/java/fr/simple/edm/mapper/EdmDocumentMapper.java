package fr.simple.edm.mapper;

import javax.inject.Named;

import fr.simple.edm.common.dto.EdmDocumentDto;
import fr.simple.edm.model.EdmDocument;

@Named
public class EdmDocumentMapper extends AbstractMapper<EdmDocument, EdmDocumentDto> {

	public EdmDocumentMapper() {
		super(EdmDocument.class, EdmDocumentDto.class);
	}

}
