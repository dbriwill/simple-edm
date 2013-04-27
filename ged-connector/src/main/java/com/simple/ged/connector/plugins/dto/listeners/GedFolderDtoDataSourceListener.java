package com.simple.ged.connector.plugins.dto.listeners;

import com.simple.ged.connector.plugins.dto.GedComponentDTO;
import com.simple.ged.connector.plugins.dto.GedFolderDTO;

import java.util.List;

/**
 * @author Xavier
 *
 * Bridge specialized for directories
 *
 */
public interface GedFolderDtoDataSourceListener extends GedComponentDtoDataSourceListener {

    /**
     * This directory want to know his children
     * 
     * @param gedFolderDTO 
     * 				The folder which you wan't to know childs
     */
    public List<GedComponentDTO> loadAndGiveMeMyChildren(GedFolderDTO gedFolderDTO);

}
