package org.vivoweb.harvester.soap;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class xmls {
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		Document doc = null;
		
		try
		{
			
			factory.setAttribute(
				"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				"http://www.w3.org/2001/XMLSchema");
			factory.setAttribute(
				"http://java.sun.com/xml/jaxp/properties/schemaSource",
				"contact.xsd");
			DocumentBuilder parser = factory.newDocumentBuilder();
			doc = parser.parse(new File("books.xml"));
			
		} catch(ParserConfigurationException e)
		{
			e.printStackTrace();
		} catch(SAXException e)
		{
			e.printStackTrace();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		return;
		
	}
	
}
