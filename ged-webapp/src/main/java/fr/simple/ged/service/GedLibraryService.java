package fr.simple.ged.service;

import java.util.ArrayList;
import java.util.List;

import fr.simple.ged.model.GedLibrary;


public class GedLibraryService {

	protected GedLibraryRepository gedLibraryRepository;
	
	protected GedLibraryConverter gedLibraryConverter;


	public List<GedLibrary> getGedLibraries() {
//		List<GedLibrary> gedLibraries = new ArrayList<>();
//		for (GedLibrary l : gedLibraryRepository.findAll()) {
//			gedLibraries.add(l);
//		}
//		return gedLibraries;
		return null;
	}
	
	
	public GedLibrary save(GedLibraryDto gedLibraryDto) {
		return null; //gedLibraryRepository.save(gedLibraryConverter.dtoToModel(gedLibraryDto));
	}
	
}
