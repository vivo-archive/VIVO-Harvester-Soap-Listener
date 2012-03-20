/*
 * $Author: Mayank Saini
 * $Date: 2012/20/03 10:53:20 $
 * ======================================================================= */

package org.vivoweb.harvester.soap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.WSDDEngineConfiguration;
import org.apache.axis.deployment.wsdd.WSDDDeployment;
import org.apache.axis.deployment.wsdd.WSDDDocument;
import org.apache.axis.server.AxisServer;
import org.apache.axis.utils.ClassUtils;
import org.apache.axis.utils.XMLUtils;
import org.apache.axis.utils.cache.ClassCache;
import org.jSoapServer.utils.FileUtils;
import org.jSoapServer.ISoapServer;
import org.jSoapServer.SoapServer;
import org.quickserver.net.AppException;
import org.quickserver.net.server.QuickServer;
import org.quickserver.util.xmlreader.QSAdminServerConfig;
import org.quickserver.util.xmlreader.QuickServerConfig;
import org.w3c.dom.Document;


public class DeployListener {

	public static void main(String args[]) {
		try {
			// creating a new soap server
		
			SoapServer myServer = new SoapServer();
			// configuring the server properties
		    String confFile =  "jSoapServer.xml";           
			
		    if (myServer.initService(confFile)) {
				myServer.deployRpcSoapService(org.vivoweb.harvester.soap.PeopleListener.class, "PeopleListener");
      			myServer.startServer();
				QSAdminServerConfig adminConfig = myServer.getConfig()
						.getQSAdminServerConfig();
				if (adminConfig != null)
					myServer.startQSAdminServer();
			}
		} catch (Exception e) {
			System.err.println("Error in server : " + e);
			e.printStackTrace();

		}
	}

}
