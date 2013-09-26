package com.simple.ged.tools;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Pour une gestion plus souple du context spring comme on est pas dans une appli JEE
 * 
 * @author Xavier
 *
 */
public class SpringFactory {

	private static ClassPathXmlApplicationContext appContext;
	
	static {
		appContext = new ClassPathXmlApplicationContext();
		appContext.getEnvironment().setActiveProfiles("resthub-jpa");
		String[] locations = { "classpath*:resthubContext.xml", "classpath*:applicationContext.xml" };
		appContext.setConfigLocations(locations);
		appContext.refresh();
	}
	
	
	public static ClassPathXmlApplicationContext getAppContext() {
		return appContext;
	}
	
	public static void close() {
		appContext.close();
	}
}
