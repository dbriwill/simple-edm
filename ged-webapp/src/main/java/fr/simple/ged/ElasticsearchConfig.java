package fr.simple.ged;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import fr.simple.ged.embedded.EmbeddedElasticSearchLauncher;

@Configuration
@EnableElasticsearchRepositories(basePackages = "fr.simple.ged.repository")
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(EmbeddedElasticSearchLauncher.getNodeClient());
    }

}
