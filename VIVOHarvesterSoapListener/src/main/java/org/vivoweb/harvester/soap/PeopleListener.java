/*

 * $Author: Mayank Saini
 * $Date: 2012/20/03 12:28:37 $
 * ======================================================================= */

package org.vivoweb.harvester.soap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This is an webservice class for people-listener
 * 
 * @author Mayank Saini
 */
public class PeopleListener {
	String filename;
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	Document doc;

	public String getPerson1(String p) throws Exception {
		String returnvalue = "NULLVALUE";
		String soapbody = null;
		MessageContext msgContext = MessageContext.getCurrentContext();
		try {
			soapbody = msgContext.getRequestMessage().getSOAPPartAsString();
			System.out.println(soapbody);
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(new ByteArrayInputStream(soapbody.getBytes()));
			Node temp = doc.getChildNodes().item(0);
			String wellformatstring = doc.getChildNodes().item(0)
					.getTextContent();
			InputStream in = new ByteArrayInputStream(
					wellformatstring.getBytes());
			if (validateXML(in)) {
				returnvalue = "ok";
			} else
				returnvalue = "BAD Format";
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnvalue;
	}

	boolean validateXML(InputStream in) throws Exception {
		boolean result;
		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		File schemaLocation = new File(
				"/home/mayank/Desktop/schma/Xsd_PERSON.xsd");
		Schema schema = factory.newSchema(schemaLocation);
		Validator validator = schema.newValidator();
		Source source = new StreamSource(in);
		try {
			validator.validate(source);
			result = true;
		} catch (SAXException E) {
			result = false;
			E.printStackTrace();
		}
		return result;
	}
}
