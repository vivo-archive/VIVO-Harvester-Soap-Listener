package org.vivoweb.harvester.soap;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class PeopleListenerTest {
    private String hostIp = null;
    private int hostPort = 8090;
    
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
            
            return (String) call.invoke(new Object[]{"mayank","A"}); 
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



}