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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.axis.MessageContext;
import org.xml.sax.SAXException;

/**
 * This is an webservice class for people-listener
 * 
 * @author Mayank Saini
 */
public class PeopleListener {

	/**
	 * Returns a teststring
	 * 
	 * @return <code>Test</code>
	 * @throws SOAPException
	 */

	public String getPerson(String p) throws SOAPException {
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

	public void testException() throws Exception {
		throw new Exception("TestException Text");
	}

	boolean validateXML() throws SAXException {

		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		File schemaLocation = new File(
				"/home/mayank/Desktop/schma/Xsd_PERSON.xsd");
		Schema schema = factory.newSchema(schemaLocation);

		Validator validator = schema.newValidator();

		Source source = new StreamSource("temp/kk");

		try {
			validator.validate(source);

		} catch (SAXException ex) {
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}
}
