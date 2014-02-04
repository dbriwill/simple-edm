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

import com.google.common.base.CaseFormat;

import fr.simple.ged.ElasticsearchConfig;
import fr.simple.ged.model.GedDocument;
import fr.simple.ged.model.GedNode;
import fr.simple.ged.repository.GedDocumentRepository;

@Service
public class GedDocumentService {

	private final Logger logger = LoggerFactory.getLogger(GedDocumentService.class);

	@Inject
	private ElasticsearchConfig elasticsearchConfig;

	@Inject
	private GedDocumentRepository gedDocumentRepository;

	@Inject
	private GedNodeService gedNodeService;

	public GedDocument findOne(String id) {
		return gedDocumentRepository.findOne(id);
	}

	public GedDocument save(GedDocument gedDocument) {
		try {

			// the document is build manualy to
			// have the possibility to add the binary file
			// content

			XContentBuilder contentBuilder = jsonBuilder();

			// add ged document attributes
			contentBuilder.startObject();

			Class<?>[] classes = new Class[] { GedNode.class, GedDocument.class };
			for (Class<?> clazz : classes) {
				for (Method m : clazz.getDeclaredMethods()) {
					if (m.getName().startsWith("get")) {
						if (m.getName().equalsIgnoreCase("getFilename")) { // ignore
																		// this
																		// type
							continue;
						}
						Object oo = m.invoke(gedDocument);
						String fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, m.getName().substring(3));
						contentBuilder.field(fieldName, oo);
					}
				}
			}

			if (! gedDocument.getFilename().isEmpty()) {
				// now add the binaries file
				logger.debug("Adding file '{}' for ES indexation", gedDocument.getFilename());
	
				contentBuilder.startObject("file");
	
				Path filePath = Paths.get(gedDocument.getFilename());
	
				String contentType = Files.probeContentType(filePath);
				String name = gedDocument.getFilename();
				String content = Base64.encodeBytes(Files.readAllBytes(filePath));
	
				contentBuilder.field("_content_type", contentType).field("_name", name).field("content", content);
	
				contentBuilder.endObject();
			}

			// and that's all folks
			contentBuilder.endObject();

			// TODO : dynamise index and type with GedDocument annotation !
			IndexResponse ir = elasticsearchConfig.getClient().prepareIndex("documents", "document", gedDocument.getId()).setSource(contentBuilder).execute().actionGet();

			gedDocument.setId(ir.getId());

			logger.debug("Indexed ged document {} with id {}", gedDocument.getId(), ir.getId());
		} catch (Exception e) {
			logger.error("Failed to index document", e);
		}

		return gedDocument;
		// return gedDocumentRepository.save(gedDocument);
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
