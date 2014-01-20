package fr.simple.ged.mapper;

import javax.inject.Named;

import fr.simple.ged.common.dto.GedNodeDto;
import fr.simple.ged.model.GedNode;

@Named
public class GedNodeMapper extends AbstractMapper<GedNode, GedNodeDto> {

	public GedNodeMapper() {
		super(GedNode.class, GedNodeDto.class);
	}

}
