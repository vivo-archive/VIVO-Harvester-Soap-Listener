package org.vivoweb.harvester.soap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.xml.sax.SAXException;

public class PeopleListenerTest {
	private String hostIp = null;
	private int hostPort = 8090;

	public PeopleListenerTest(String ip, int port) {
		this.hostIp = ip;
		this.hostPort = port;
	}

	public String execTest() {
		try {
			String endpoint = "http://" + this.hostIp + ":" + this.hostPort
					+ "/PeopleListener";
			System.out.println(endpoint);

			Service service = new Service();
			Call call = (Call) service.createCall();
		//	String value = getStringFromDoc("/home/mayank/Desktop/schma/Xsd_PERSON_output.xml");
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName(endpoint, "getName"));

			return  (String) call.invoke(new Object[]{"mayank","saini"});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		PeopleListenerTest soapCallTest = new PeopleListenerTest("localhost",
				8090);
		String dateString = soapCallTest.execTest();
		if (dateString != null) {
			System.out.println("Date: " + dateString);
		}
	}

	public String getStringFromDoc(String fileName) throws TransformerFactoryConfigurationError, SAXException, IOException, ParserConfigurationException, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		//initialize StreamResult with File object to save to file
		StreamResult result = new StreamResult(new StringWriter());
        InputStream inputStream = new FileInputStream(new File(fileName));
        org.w3c.dom.Document doc = documentBuilderFactory.newDocumentBuilder().parse(inputStream);
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);

		String xmlString = result.getWriter().toString();
		System.out.println(xmlString);
		return xmlString;
	}
}
