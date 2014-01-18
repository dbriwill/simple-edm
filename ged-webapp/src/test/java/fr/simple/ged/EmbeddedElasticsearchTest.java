package fr.simple.ged;

import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.reflect.Field;

import org.elasticsearch.node.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import fr.simple.ged.embedded.EmbeddedElasticSearchLauncher;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { Application.class })
@ComponentScan(basePackages = { "fr.simple.ged" })
public class EmbeddedElasticsearchTest {

	@Test
	public void nodeShouldBeStartedAndWorking() {
		Field field = null;
		try {
			field = EmbeddedElasticSearchLauncher.class.getDeclaredField("node");
		} catch (Exception e) {
			e.printStackTrace();
		}

		field.setAccessible(true);

		Node node = null;
		try {
			node = (Node) field.get(new EmbeddedElasticSearchLauncher());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		EmbeddedElasticSearchLauncher.start();

		assertThat(node).isNotNull();
		assertThat(node.isClosed()).isFalse();
	}
	
	
	@Test
	public void esShouldStoppable() {
		Field field = null;
		try {
			field = EmbeddedElasticSearchLauncher.class.getDeclaredField("node");
		} catch (Exception e) {
			e.printStackTrace();
		}

		field.setAccessible(true);

		Node node = null;
		try {
			node = (Node) field.get(new EmbeddedElasticSearchLauncher());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		EmbeddedElasticSearchLauncher.start();
		EmbeddedElasticSearchLauncher.stop();

		assertThat(node).isNotNull();
		assertThat(node.isClosed()).isTrue();
	}
}
