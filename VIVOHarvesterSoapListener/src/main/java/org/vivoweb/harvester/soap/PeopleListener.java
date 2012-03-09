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

import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.attachments.Attachments;
import org.apache.axis.attachments.PlainTextDataSource;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPHeaderElement;

/**
 * This is an  webservice class for people-listener
 * @author Mayank Saini
 */
public class PeopleListener {
    
    /**
     * Returns a teststring
     * @return <code>Test</code>
     */
    public String testString() {
        return "Test";
    }
    
    public DataHandler testAttachment(DataHandler attachment) {
    	return null;
    }
    
    public boolean testBoolean() {
        return true;
    }
    
    /**
     * Returns the current system date
     * @return current date
     */
    public Date testDate() {
        return new Date();
    }
    
    /**
     * Adds to integer values and returns the result
     * @param a
     * @param b
     * @return <code>a + b</code>
     */
    public int testIntAdd(int a, int b) {
        return a+b;
    }
    public String getName(String str, String str2) {
        return "Hello"+str +"   "+str2;
    }
    
    /**
     * Returns username and password that was set by the client 
     * via http authentication 
     * @return username, password e.g. <code>testUser:testPwd</code>
     */
    public String testUserNamePwd() {
        // getting the current message context
        MessageContext msgContext = MessageContext.getCurrentContext();
        
        // getting username and password set via http authentication
        String userName = msgContext.getUsername();
        String pwd = msgContext.getPassword();
        
        return userName + ":" + pwd;        
    }
    
    /**
     * Returns username and password that was set by the client
     * via custom soap headers
     * @return username, password. e.g. <code>testUser:testPwd</code>
     * @throws AxisFault
     */
    public String testSoapHeader() throws AxisFault {
        // getting the current message context
        MessageContext msgContext = MessageContext.getCurrentContext();
        
        // getting the request message
        Message message = msgContext.getRequestMessage();
        
        // getting the request envelope
        SOAPEnvelope envelope = message.getSOAPEnvelope();
        
        // getting our custom soap header
        SOAPHeaderElement header = envelope.getHeaderByName("http://jSoapServer.org/securityTest", "securityHeader");
        MessageElement user = header.getChildElement(new QName("http://jSoapServer.org/securityTest","username"));
        MessageElement pwd = header.getChildElement(new QName("http://jSoapServer.org/securityTest","password"));
        
        return user.getValue() + ":" + pwd.getValue();
    }
    
    /**
     * @throws Exception 
     */
    public void testException() throws Exception {
        throw new Exception("TestException Text");        
    }
    
    /**
     * Returns the content of a file that was send by the
     * client via soap attachment
     * @return
     * @throws Exception
     */
    public String testReceiveAttachment() throws Exception {
        // getting the current message context
        MessageContext msgContext = MessageContext.getCurrentContext();

        // getting the request message
        Message reqMsg = msgContext.getRequestMessage();
        
        // getting the attachment implementation
        Attachments messageAttachments = reqMsg.getAttachmentsImpl();
        if (messageAttachments == null) {
            throw new Exception("Attachments not supported");
        }
        
        // getting the type of the attachment
        // int inputAttachmentType = messageAttachments.getSendType();
        
        // how many attachments are there?
        int attachmentCount= messageAttachments.getAttachmentCount();
        if (attachmentCount == 0) 
            throw new Exception("No attachment found");
        else if (attachmentCount != 1)
            throw new Exception("Too many attachments as expected.");
        
        // getting the attachments
        AttachmentPart[] attachments = (AttachmentPart[])messageAttachments.getAttachments().toArray(new AttachmentPart[attachmentCount]);
        
        // getting the content of the attachment
        DataHandler dh = attachments[0].getDataHandler();
        Object content = dh.getContent();
        
        return (String)content;
    }
    
    public Object testSendAttachment(int attachmentType, String message) throws Exception {
        // getting the current message context
        MessageContext msgContext = MessageContext.getCurrentContext();

        // getting the response message object
        Message respMsg = msgContext.getResponseMessage();        
        
        // getting the attachment implementation
        Attachments messageAttachments = respMsg.getAttachmentsImpl();
        if (messageAttachments == null) {
            throw new Exception("Attachments not supported");
        }        
        messageAttachments.setSendType(attachmentType);

        // creating a datahandler for the attachment
        DataSource data =  new PlainTextDataSource("Test.txt",message);
        DataHandler attachmentFile = new DataHandler(data);     
        
        // return the attachment as result
        return attachmentFile;
    }
    
}
