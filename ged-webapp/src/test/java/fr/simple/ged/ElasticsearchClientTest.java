package fr.simple.ged;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { Application.class })
@ComponentScan(basePackages = { "fr.simple.ged" })
public class ElasticsearchClientTest {

	@Test
	public void nodeShouldBeStartedAndWorking() {

	}
	
	
	@Test
	public void esShouldStoppable() {

	}
}
