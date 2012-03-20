package org.vivoweb.harvester.soap;

import java.io.*;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.xml.sax.SAXException;

public class SchemaValidator {

	public static void main(String[] args) throws SAXException, IOException {

		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");

		File schemaLocation = new File(
				"/home/mayank/Desktop/schma/Xsd_PERSON.xsd");
		Schema schema = factory.newSchema(schemaLocation);

		Validator validator = schema.newValidator();

		javax.xml.transform.Source source = new StreamSource("/home/mayank/Desktop/schma/Xsd_PERSON_output.xml");

		try {
			validator.validate(source);
			System.out.println("/home/mayank/Desktop/schma/Xsd_PERSON_output.xml" + " is valid.");
		} catch (SAXException ex) {
			System.out.println(" is not valid because ");
			System.out.println(ex.getMessage());
		}

	}

}