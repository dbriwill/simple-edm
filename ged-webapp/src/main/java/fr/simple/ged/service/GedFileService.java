package fr.simple.ged.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import fr.simple.ged.model.GedFile;
import fr.simple.ged.repository.GedFileRepository;

@Service
public class GedFileService {

	@Inject
	private GedFileRepository gedFileRepository;

	public GedFile findOne(String id) {
		return gedFileRepository.findOne(id);
	}

	public GedFile save(GedFile gedFile) {
		// TODO [improve me] ; see https://github.com/spring-projects/spring-data-elasticsearch/issues/21 and https://github.com/spring-projects/spring-data-elasticsearch/pull/27
		// unless it's fixed, I set my generated ID
		if (gedFile.getId() == null || gedFile.getId().isEmpty()) {
			gedFile.setId(String.valueOf(System.currentTimeMillis()));
		}
		return gedFileRepository.index(gedFile);
	}

}
