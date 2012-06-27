package org.vivoweb.test.harvester.soap;
import java.io.File;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.vivoweb.harvester.soap.VIVOWebServicesListener;
import org.vivoweb.harvester.soap.WebServerSingleton;

public class PeopleListenerTest extends TestCase {
    private String hostIp = "localhost";
    private int hostPort = 8090;
    
    public PeopleListenerTest(){}
    
    public PeopleListenerTest(String ip, int port) {
        this.hostIp = ip;
        this.hostPort = port;
    }
    
    public String execTest() {
        try {
            String endpoint = "http://" + this.hostIp + ":" + this.hostPort + "/PeopleListener";
            
            Service  service = new Service();
            Call call = (Call) service.createCall();
            
            call.setTargetEndpointAddress( new java.net.URL(endpoint));
            call.setOperationName(new QName(endpoint, "getName"));
            
            return (String) call.invoke(new Object[]{"mayank","saini"}); 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args) {
    	PeopleListenerTest soapCallTest = new PeopleListenerTest("localhost",8090);
    	String dateString = soapCallTest.execTest();
        if (dateString != null) {
            System.out.println("Date: " + dateString);
        }
    }    

    public final static String incomingMessage =
    		"<ns0:PERSON xmlns:ns0=\"http://uf.biztalk.shibperson\">" + 
			    "<UFID>83117145</UFID>" +
			    "<GLID>vsposato</GLID>" +
			    "<UFID2 />" +
			    "<GLID2 />" +
			    "<ACTIVE>A</ACTIVE>" +
			    "<PROTECT>N</PROTECT>" +
			    "<AFFILIATION>T</AFFILIATION>" +
			    "<NAME type=\"21\">SPOSATO%2cVINCENT%20J</NAME>" +
			    "<NAME type=\"33\">Sposato%2cVincent%20J</NAME>" +
			    "<NAME type=\"35\">Vincent</NAME>" +
			    "<NAME type=\"36\">Sposato</NAME>" +
			    "<NAME type=\"37\">J</NAME>" +
			    "<NAME type=\"232\">Sposato%2cVincent</NAME>" +
			    "<ADDRESS>" +
			      "<ADDRESS1 />" +
			      "<ADDRESS2 />" +
			      "<ADDRESS3>PO%20BOX%20100152</ADDRESS3>" +
			      "<CITY>GAINESVILLE</CITY>" +
			      "<STATE>FL</STATE>" +
			      "<ZIP>326100152</ZIP>" +
			    "</ADDRESS>" +
			    "<PHONE type=\"10\">(352)%20294-5274%20%20%2045274</PHONE>" +
			    "<EMAIL type=\"1\">vsposato%40ufl.edu</EMAIL>" +
			    "<DEPTID>27010707</DEPTID>" +
			    "<RELATIONSHIP type=\"195\">" +
			      "<DEPTID>27010707</DEPTID>" +
			      "<DEPTNAME>HA-AHC%20ESE</DEPTNAME>" +
			    "</RELATIONSHIP>" +
			    "<RELATIONSHIP type=\"203\">" +
			      "<DEPTID>27010707</DEPTID>" +
			      "<DEPTNAME>HA-AHC%20ESE</DEPTNAME>" +
			    "</RELATIONSHIP>" +
			    "<RELATIONSHIP type=\"220023\">" +
			      "<DEPTID>27010707</DEPTID>" +
			      "<DEPTNAME>HA-AHC%20ESE</DEPTNAME>" +
			    "</RELATIONSHIP>" +
			    "<WORKINGTITLE>IT%20Expert%2C%20Sr.%20Software%20Engineer</WORKINGTITLE>" +
			    "<DECEASED>N</DECEASED>" +
			    "<LOA>Bronze</LOA>" +
			    "<ACTION>CREATE</ACTION>" +
			  "</ns0:PERSON>";
    
    public void testSpecificNaming() throws Exception
    {
    	String folderPath = "/tmp/wslProcessedMsgs/";
    	String logPath = "/tmp/wslLog/";
    	String schemaFile = "PERSON.xsd";
    	
    	// Create a file object to test the schemaFile with
		File testSchema = new File(schemaFile);

		// Check to see if the schemaFile a) is a file, b) can be read, and c) exists
		if (!testSchema.isFile() || !testSchema.canRead() || !testSchema.exists()) {
			throw new IllegalArgumentException("Schema file is either not a file, does not exist, or cannot be read from!");
		}

		// Create a file object to test the output folderPath with
		File testFolderPath = new File(folderPath);

		// Check to see if the output folderPath a) is a directory, b) can be written, and c) exists
		if (!testFolderPath.isDirectory() || !testFolderPath.canWrite()	|| !testFolderPath.exists()) {
			throw new IllegalArgumentException("Output folder path is either not a directory, does not exist, or cannot be written to!");
		}
    	
    	WebServerSingleton.getInstance();
		WebServerSingleton.setProperty("schemaFile", schemaFile);
		WebServerSingleton.setProperty("folderPath", folderPath);
		WebServerSingleton.setProperty("logDir", logPath);
		WebServerSingleton.setProperty("bSpecificNaming", "true");
		WebServerSingleton.setProperty("namingExpression", "//UFID");
		
		VIVOWebServicesListener vwslTest = new VIVOWebServicesListener();
		vwslTest.messageTest(incomingMessage);
    }











}