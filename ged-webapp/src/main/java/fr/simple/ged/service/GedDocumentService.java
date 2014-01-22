package fr.simple.ged.service;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.Base64;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.simple.ged.model.GedDocument;
import fr.simple.ged.model.GedFile;
import fr.simple.ged.repository.GedDocumentRepository;

@Service
public class GedDocumentService {

	private final Logger logger = LoggerFactory.getLogger(GedDocumentService.class);

	@Inject
	private GedDocumentRepository gedDocumentRepository;

	
	public GedDocument findOne(String id) {
		return gedDocumentRepository.findOne(id);
	}

	public GedDocument save(GedDocument gedDocument) {
		// TODO [improve me] ; see https://github.com/spring-projects/spring-data-elasticsearch/issues/21 and https://github.com/spring-projects/spring-data-elasticsearch/pull/27
		// unless it's fixed, I set my generated ID
		if (gedDocument.getId() == null || gedDocument.getId().isEmpty()) {
			gedDocument.setId(String.valueOf(System.currentTimeMillis()));
		}

//		try {
//
//			// the document is build manualy to
//			// have the possibility to add the binary file
//			// content
//
//			XContentBuilder contentBuilder = jsonBuilder();
//
//			// add ged document attributes
//			contentBuilder.startObject();
//
//			for (Method m : GedDocument.class.getDeclaredMethods()) {
//				if (m.getName().startsWith("get")) {
//					if (m.getName().equalsIgnoreCase("getFiles")) { // ignore this type
//						continue;
//					}
//					Object oo = m.invoke(gedDocument);
//					contentBuilder.field(m.getName().substring(3), oo);
//				}
//			}
//
//			// now add the binaries files
//
//			if (gedDocument.getFiles().size() > 0) {
//
//				contentBuilder.startArray("files");
//
//				for (GedFile gedFile : gedDocument.getFiles()) {
//
//					logger.debug("Adding file '{}' for ES indexation",	gedFile.getFileName());
//
//					contentBuilder.startObject();
//
//					Path filePath = Paths.get(gedFile.getRelativeFilePath());
//
//					String contentType = Files.probeContentType(filePath);
//					String name = gedFile.getFileName();
//					String content = Base64.encodeBytes(Files.readAllBytes(filePath));
//
//					contentBuilder.field("_content_type", contentType).field("_name", name).field("content", content);
//
//					contentBuilder.endObject();
//				}
//
//				contentBuilder.endArray();
//			}
//
//			// and that's all folks
//			contentBuilder.endObject();
//
//			// TODO : dynamise index and type with GedDocument annotation !
//			IndexResponse ir = node.client().prepareIndex("documents", "document", gedDocument.getId()).setSource(contentBuilder).execute().actionGet();
//
//			logger.debug("Indexed ged document {} with id {}", gedDocument.getId(), ir.getId());
//		} catch (Exception e) {
//			logger.error("Failed to index document", e);
//		}

		return gedDocumentRepository.index(gedDocument);
	}

	
	public List<GedDocument> search(String pattern) {
		BoolQueryBuilder qb = QueryBuilders.boolQuery();

		for (String word : pattern.split(" ")) {
			qb.must(QueryBuilders.fuzzyLikeThisQuery("_all").likeText(word));
		}

		logger.debug("The search query for pattern '{}' is : {}", pattern, qb);

		return Lists.newArrayList(gedDocumentRepository.search(qb));
	}

}
