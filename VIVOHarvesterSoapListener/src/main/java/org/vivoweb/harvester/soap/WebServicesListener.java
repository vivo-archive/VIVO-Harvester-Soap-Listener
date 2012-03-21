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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This is an webservice class for people-listener
 * 
 * @author Mayank Saini,sandhya,kuppu
 */

public class WebServicesListener {

	String filename; // File name for the received Message
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	Document doc;

	private String folderPath; // Dir path where received xml message will be
								// stored

	private String schemaFile;// Location of XSD file to validate xml message
								// against

	/**
	 * Get Message is the webservice which will listen for SOAP XML messages
	 * 
	 * @param Soapbody
	 *            payload in form URL encoded string
	 * @return
	 * @throws SOAPException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 *             // This exception will be thrown if the received XML does not
	 *             match with specified XSD
	 */

	public String getMessage(String p) throws SOAPException, SAXException, IOException,
			ParserConfigurationException {

		// WebServerSingleton
		// This is the Singleton Object to store and access the webserver config
		// properties . This is based on the singleton pattern and a new Object
		// will be created only when you restart the server

		this.schemaFile = (String) WebServerSingleton.getProperty("schemaFile");
		this.folderPath = (String) WebServerSingleton.getProperty("folderPath");
		Date date = new Date();

		// Get number of milliseconds since January 1, 1970, 00:00:00 GMT
		// represented by this Date object.
		String filename = new Long(date.getTime()).toString();

		// Use above time to generate Uniqueu filename for received messages
		filename = folderPath + "/" + filename;

		String returnvalue = "NULLVALUE";
		String soapbody = null;

		// SOAPMessageContext provides access to the SOAP message for either
		// request or response.
		// from this we can get the reference of soapbody and other required
		// tags if needed
		MessageContext msgContext = MessageContext.getCurrentContext();
		try {

			// get the soap body from the Soap message as String
			soapbody = msgContext.getRequestMessage().getSOAPPartAsString();
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();

			// Build the DOM document from the Soap body, so that we can extract
			// a specific reuired section of DOM using SAX parser
			doc = dBuilder.parse(new ByteArrayInputStream(soapbody.getBytes()));

			// Get the text content of Soap body payload. This will be actual
			// received message
			String wellformatstring = doc.getChildNodes().item(0)
					.getTextContent();

			wellformatstring = wellformatstring.trim();

			// Place the \n between every > < so that I will look nice

			wellformatstring = wellformatstring.replaceAll("> * <", ">\n<");

			InputStream in = new ByteArrayInputStream(
					wellformatstring.getBytes());

			// Validate to see if Received message is as per specified XSD

			if (validateXML(in)) {

				returnvalue = "ok";
				// Write the OutPUT file
				BufferedWriter out = new BufferedWriter(
						new FileWriter(filename));
				out.write(wellformatstring);
				out.close();

			} else {// if the format is BAD
				returnvalue = "BAD Format";

			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnvalue;
	}

	
	/*** Validate XML validate the XML  data against the XSD file
	 * 
	 * @param in
	 * @return
	 * @throws SAXException
	 */
	
	protected boolean validateXML(InputStream in) throws SAXException {

		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = factory.newSchema(new File(schemaFile));

		Validator saxValidator = schema.newValidator();

		Source source = new StreamSource(in);

		try {
			saxValidator.validate(source);

		} catch (SAXException ex) {
			ex.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}
}
