/*
 *  jSoapServer is a Java library implementing a multi-threaded
 *  soap server which can be easily integrated into java applications
 *  to provide a SOAP Interface for external programmers.
 *  
 *  Copyright (C) 2007 Martin Thelian
 *  
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *  
 *  For more information, please email thelian@users.sourceforge.net
 */

/* =======================================================================
 * Revision Control Information
 * $Source: /cvsroot/jsoapserver/jSoapServer/src/main/java/org/jSoapServer/SoapService.java,v $
 * $Author: thelian $
 * $Date: 2010/05/05 12:28:37 $
 * $Revision: 1.1 $
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
