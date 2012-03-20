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
 * $Source: /cvsroot/jsoapserver/jSoapServer/src/main/java/org/jSoapServer/SoapServer.java,v $
 * $Author: thelian $
 * $Date: 2010/05/06 10:53:20 $
 * $Revision: 1.3 $
 * ======================================================================= */

package org.jSoapServer;

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


public class DeployPeopleListener {

	public static void main(String args[]) {
		try {
			// creating a new soap server
		
			SoapServer myServer = new SoapServer();
			// configuring the server properties
		    String confFile =  "jSoapServer.xml";           
			
		    if (myServer.initService(confFile)) {
				myServer.deployRpcSoapService(org.jSoapServer.PeopleListener.class, "PeopleListener");
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
