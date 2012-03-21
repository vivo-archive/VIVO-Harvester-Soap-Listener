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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

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
public class VIVOWebServicesListener {
	String filename;
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	Document doc;
	
	/**
	 * 
	 */
	private File folderPath = new File("/home/vsposato/web_service");
	
	/**
	 * 
	 */
	private File schemaFile = new File("/home/vsposato/web_service/PERSON.xsd");
	
	/*public VIVOWebServicesListener(String folderPath, String schemaFile) {
		
		
		this.folderPath = new File(folderPath);
		this.schemaFile = new File(schemaFile);
		
		
		if ( ! this.folderPath.isDirectory() || ! this.folderPath.canWrite() ) {
			throw new IllegalArgumentException("Output directory does not exist, is not writeable, or is not a directory!");
		}
		
		if ( ! this.schemaFile.exists() || this.schemaFile.isDirectory() || ! this.schemaFile.canRead() ) {
			throw new IllegalArgumentException("Schema file does not exist!");
		}
 	}*/
	
	/**
	 * Returns a teststring
	 * 
	 * @return <code>Test</code>
	 * @throws SOAPException
	 */
	
	public String getMessageOld(SOAPElement p) throws SOAPException {
		OutputStream outFile = null;
		PrintWriter out;
		try {
			outFile = new FileOutputStream(new File("raw-record/soap.xml"));
			out = new PrintWriter(outFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("===============================");
		MessageContext msgContext = MessageContext.getCurrentContext();
		SOAPMessage msg = msgContext.getMessage();

		System.out.println(msg.getProperty("Person").toString());
		try {
			msg.writeTo(outFile);

		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Message Context is" + msgContext.toString());

		System.out.println("===============================");
		try {
			outFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "hello";

	}

	public String getMessage(String p) throws SOAPException, SAXException, IOException, ParserConfigurationException {
		Date date = new Date();
		String filename = date.toString();
		filename = filename.replaceAll(" ", "_");
		filename = filename.replaceAll(":", "_");
		filename = "/home/mayank/Desktop/data/"+filename;
		System.out.println(filename);

		String returnvalue = "NULLVALUE";
		String soapbody = null;
		MessageContext msgContext = MessageContext.getCurrentContext();
		try {
			soapbody = msgContext.getRequestMessage().getSOAPPartAsString();
			System.out.println(soapbody);
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(new ByteArrayInputStream(soapbody.getBytes()));
			String wellformatstring = doc.getChildNodes().item(0).getTextContent();
			wellformatstring=wellformatstring.trim();
			wellformatstring= wellformatstring.replaceAll("> * <", ">\n<");
			InputStream in = new ByteArrayInputStream(
					wellformatstring.getBytes());
			if (validateXML(in)) {
				returnvalue = "ok";
				BufferedWriter out = new BufferedWriter(new FileWriter(filename));
				out.write(wellformatstring);
				out.close();

			} else
				returnvalue = "BAD Format";
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnvalue;
	}

	protected boolean validateXML(InputStream in ) throws SAXException {

		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = factory.newSchema(this.schemaFile);

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
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}
}
