package org.vivoweb.harvester.soap;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.jSoapServer.SoapHandler;
import org.quickserver.util.logging.SimpleTextFormatter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This is an webservice class for a generic XML message SOAP receiver
 * 
 * @author Mayank Saini, Sandhya Komaragiri, Kuppuraj Gunasekaran, Vincent
 *         Sposato, Stephen Williams
 * 
 */

public class VIVOWebServicesListener {
	private static Logger logger = Logger.getLogger(VIVOWebServicesListener.class
			.getName());
	String filename;

	/**
	 * folderPath that we will output our XML message
	 */
	private File folderPath;

	/**
	 * schemaFile this is the schemaFile that we will use to validate the XML
	 * message against
	 */
	private File schemaFile;

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
	public String getMessage(String p) throws SOAPException, SAXException,
			IOException, ParserConfigurationException {


		
		// WebServerSingleton
		// This is the Singleton Object to store and access the webserver config
		// properties . This is based on the singleton pattern and a new Object
		// will be created only when you restart the server

		this.schemaFile = new File(
				(String) WebServerSingleton.getProperty("schemaFile"));
		this.folderPath = new File(
				(String) WebServerSingleton.getProperty("folderPath"));

		// Get number of milliseconds since January 1, 1970, 00:00:00 GMT
		// represented by this Date object.
		String filename = new Long(new Date().getTime()).toString();

		// Use above time to generate Uniqueu filename for received messages
		filename = this.folderPath + "/" + filename;

		String returnValue = "NULLVALUE";
		String soapBody = null;

		// SOAPMessageContext provides access to the SOAP message for either
		// request or response.
		// From this we can get the reference of soapbody and other required
		// tags if needed
		MessageContext msgContext = MessageContext.getCurrentContext();
		String clientaddress = (String) msgContext
				.getProperty("CLIENT_ADDRESS");
		System.out.println("clientaddress" + clientaddress);
		try {

			// get the soap body from the Soap message as String
			soapBody = msgContext.getRequestMessage().getSOAPPartAsString();
			// soapBody= URLDecoder.decode(soapBody,"UTF-8");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			// Build the DOM document from the Soap body, so that we can extract
			// a specific reuired section of DOM using SAX parser
			Document doc = dBuilder.parse(new ByteArrayInputStream(soapBody
					.getBytes()));

			// Get the text content of Soap body payload. This will be actual
			// received message
			// TODO: Explain why item(0) and not all child nodes
			String wellFormattedString = doc.getChildNodes().item(0)
					.getTextContent();
			String copystring = doc.getChildNodes().item(0).getTextContent();

			// trimming whitespace and placing the \n between every > < so that
			// it will be human readable

			wellFormattedString = URLDecoder.decode(wellFormattedString,
					"UTF-8");
			wellFormattedString = wellFormattedString.trim();
			wellFormattedString = wellFormattedString.replaceAll("><", ">\n<");
			System.out.println(wellFormattedString);
			// format String to an inputstream to be read by sax
			InputStream in = new ByteArrayInputStream(copystring.getBytes());

			// Validate to see if Received message is as per specified XSD
			if (validateXML(in)) {

				// Write the OutPUT file
				BufferedWriter out = new BufferedWriter(
						new FileWriter(filename));
				out.write(wellFormattedString);
				out.close();
				String id=filename.split("/")[5];
				logger.fine("Message Received : From " + clientaddress
						+ " Message ID :" + id);
				returnValue = "Message Received : From " + clientaddress
						+ " Message ID :" + id; // TODO: Is this the proper format? Should
									// probably format in such a way that if a
									// bad write out occurs it doesn't send
									// success
			} else {// if the format is BAD

				logger.fine("Message Rejected : From " + clientaddress
						+ " XML Message does not match to specified  Person XSD schema");
				returnValue = "Message Rejected : From " + clientaddress
						+ " XML Message doest not match to specified  Person XSD schema"; 
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnValue;

	}

	protected boolean validateXML(InputStream in) throws SAXException {

		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}
}
