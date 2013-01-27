package com.simple.ged.connector.plugins.worker;

import com.simple.ged.connector.plugins.dto.GedDirectoryDTO;

/**
 * This class is the super class of each simple GED worker plugins
 * 
 * A worker is a plugin which action is to made some action only one time (when the user required it)
 * 
 * @author xavier
 */
public interface SimpleGedWorkerPlugin {

	/**
	 * Make your works !
	 * 
	 * @param gedRoot
	 * 				The library root as an entry point
	 */
	public void doWork(GedDirectoryDTO gedRoot);
	
}