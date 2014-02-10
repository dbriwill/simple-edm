package fr.simple.edm.mapper;

import javax.inject.Named;

import fr.simple.edm.common.dto.EdmNodeDto;
import fr.simple.edm.model.EdmNode;

@Named
public class EdmNodeMapper extends AbstractMapper<EdmNode, EdmNodeDto> {

	public EdmNodeMapper() {
		super(EdmNode.class, EdmNodeDto.class);
	}

}
