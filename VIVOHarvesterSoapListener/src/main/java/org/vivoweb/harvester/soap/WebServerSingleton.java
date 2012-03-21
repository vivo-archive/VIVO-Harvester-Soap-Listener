package org.vivoweb.harvester.soap;

import java.util.HashMap;

// WebServerSingleton
// This is the Singleton Object to store and access the webserver config
// properties . This is based on the singleton pattern and a new Object
// will be created only when you restart the server
public class WebServerSingleton {

	private static HashMap<String, String> propertyMap = new HashMap<String, String>();

	private final static WebServerSingleton INSTANCE = new WebServerSingleton();

	// No one else can create a Object .. since constructor is private
	private WebServerSingleton() {
	}

	public static WebServerSingleton getInstance() {
		return INSTANCE;
	}

	public static HashMap<String, String> getpropertyMap() {
		return propertyMap;
	}

	public static String getProperty(String name) {
		return propertyMap.get(name);
	}

	public static String setProperty(String name, String value) {
		return propertyMap.put(name, value);
	}
}
