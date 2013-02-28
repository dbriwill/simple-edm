package com.simple.ged.connector.plugins.dto.listeners;

import com.simple.ged.connector.plugins.dto.GedComponentDTO;

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
     */
    public List<GedComponentDTO> loadAndGiveMeMyChildren();

}
