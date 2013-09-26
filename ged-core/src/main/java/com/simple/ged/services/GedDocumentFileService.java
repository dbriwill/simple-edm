package com.simple.ged.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.simple.ged.dao.GedDocumentFileRepository;
import com.simple.ged.models.GedDocumentFile;
import com.simple.ged.tools.SpringFactory;


@Service
public class GedDocumentFileService {

	private GedDocumentFileRepository gedDocumentFileRepository;
	
	private GedDocumentFileService() {
		gedDocumentFileRepository = SpringFactory.getAppContext().getBean(GedDocumentFileRepository.class);
	}
	
	
	public GedDocumentFile findByRelativeFilePath(String relativefilePath) {
		return gedDocumentFileRepository.findByRelativeFilePath(relativefilePath);
	}
	
	
	public List<GedDocumentFile> findByRelativeFilePathStartingWith(String relativefilePath) {
		return gedDocumentFileRepository.findByRelativeFilePathStartingWith(relativefilePath);
	}

	
	public void save(GedDocumentFile gedDocumentFile) {
		gedDocumentFileRepository.saveAndFlush(gedDocumentFile);
	}
	
	public void delete(GedDocumentFile gedDocumentFile) {
		gedDocumentFileRepository.delete(gedDocumentFile);
	}
}
