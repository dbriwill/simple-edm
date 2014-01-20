package fr.simple.ged.mapper;

import javax.inject.Named;

import fr.simple.ged.common.dto.GedDocumentDto;
import fr.simple.ged.model.GedDocument;

@Named
public class GedDocumentMapper extends AbstractMapper<GedDocument, GedDocumentDto> {

	public GedDocumentMapper() {
		super(GedDocument.class, GedDocumentDto.class);
	}

}
