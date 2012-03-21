package org.vivoweb.harvester.soap;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vivoweb.harvester.fetch.WOSFetch;
import org.vivoweb.harvester.util.InitLog;
import org.vivoweb.harvester.util.args.ArgDef;
import org.vivoweb.harvester.util.args.ArgList;
import org.vivoweb.harvester.util.args.ArgParser;
import org.vivoweb.harvester.util.args.UsageException;
import org.vivoweb.harvester.util.repo.RecordHandler;
import org.jSoapServer.*;
//import org.quickserver.*;
//import org.quickserver.util.*;
import org.quickserver.util.xmlreader.*;

public class DeployWebServicesListener {

	/**
	 * SLF4J Logger
	 */
	private static Logger log = LoggerFactory.getLogger(DeployWebServicesListener.class);
	/**
	 * RecordHandler to put data in.
	 */
	private RecordHandler outputRH;
	
	/**
	 * 
	 */
	private String decryption;
	
	/**
	 * 
	 */
	private String folderPath;
	
	/**
	 * 
	 */
	private String soapConfigPath;
	
	/**
	 * 
	 */
	private String serviceName;
	
	/**
	 * 
	 */
	private String schemaFile;
	
	/**
	 * Command line Constructor
	 * @param args commandline arguments
	 * @throws IOException error creating task
	 * @throws UsageException user requested usage message
	 */
	private DeployWebServicesListener(String[] args) throws IOException, UsageException {
		this(getParser().parse(args));
	}
	
	/**
	 * Arglist Constructor
	 * @param args option set of parsed args
	 * @throws IOException error creating task
	 */
	private DeployWebServicesListener(ArgList args) throws IOException {
		this(
			args.get("d"), args.get("o"), args.get("c"), args.get("s"), args.get("x")
		);
	}
	/**
	 * Library style Constructor
	 */
	public DeployWebServicesListener(String decryption, String folderPath, String soapConfigPath, String serviceName, String schemaFile){
		
		this.decryption = decryption;
		this.folderPath = folderPath;
		this.soapConfigPath = soapConfigPath;
		this.serviceName = serviceName;
		this.schemaFile = schemaFile;
		
		WebServerSingleton.getInstance().setProperty("schemaFile", schemaFile);
		WebServerSingleton.getInstance().setProperty("folderPath", folderPath);
	}
	
	/**
	 * Executes the task
	 * @throws IOException error processing record handler
	 */
	public void execute() throws IOException {
		try {
			// creating a new soap server
		
			SoapServer myServer = new SoapServer();
			
		    if (myServer.initService(this.soapConfigPath)) {
		    	myServer.deployRpcSoapService(org.vivoweb.harvester.soap.WebServicesListener.class,this.serviceName);
				//myServer.deployRpcSoapService(org.jSoapServer.WebServicesListener.class, "PeopleListener");
      			myServer.startServer();
				QSAdminServerConfig adminConfig = myServer.getConfig().getQSAdminServerConfig();
				if (adminConfig != null) {
					myServer.startQSAdminServer();
				}
		    }
		} catch (Exception e) {
			System.err.println("Error in server : " + e);
			e.printStackTrace();
		}
	
	}
	
	/**
	 * Get the ArgParserfdgdgfdgfd for this task
	 * @return the ArgParser
	 */
	private static ArgParser getParser() {
		ArgParser parser = new ArgParser("DeployWebServicesListener");
		parser.addArgument(new ArgDef().setShortOption('d').setLongOpt("decrypt").withParameter(true, "DECRYPT").setDescription("The SEARCHMESSAGE file path.").setRequired(true));
		parser.addArgument(new ArgDef().setShortOption('c').setLongOpt("configFile").withParameter(true, "CONFIG").setDescription("The location of jSoapServer config file").setRequired(true));
		parser.addArgument(new ArgDef().setShortOption('o').setLongOpt("output").withParameter(true, "OUTPUT_FOLDER").setDescription("result file folder path").setRequired(true));
		parser.addArgument(new ArgDef().setShortOption('s').setLongOpt("serviceName").withParameter(true, "SERVICE_NAME").setDescription("The name of the published web service").setRequired(true));
		parser.addArgument(new ArgDef().setShortOption('O').setLongOpt("outputOverride").withParameterValueMap("RH_PARAM", "VALUE").setDescription("override the RH_PARAM of output recordhandler using VALUE").setRequired(false));
		parser.addArgument(new ArgDef().setShortOption('x').setLongOpt("schemaFile").withParameter(true,"SCHEMA_FILE").setDescription("This is the xsd file to compare your incoming xml against").setRequired(false));
		return parser;
	}
	
	/**
	 * Main method
	 * @param args commandline arguments
	 */
	public static void main(String... args) {
		Exception error = null;
		try {
			InitLog.initLogger(args, getParser());
			log.info(getParser().getAppName() + ": Start");
			new DeployWebServicesListener(args).execute();
		} catch(IllegalArgumentException e) {
			log.error(e.getMessage());
			log.debug("Stacktrace:",e);
			System.out.println(getParser().getUsage());
			error = e;
		} catch(UsageException e) {
			log.info("Printing Usage:");
			System.out.println(getParser().getUsage());
			error = e;
		} catch(Exception e) {
			log.error(e.getMessage());
			log.debug("Stacktrace:",e);
			error = e;
		} finally {
			log.info(getParser().getAppName() + ": End");
			if(error != null) {
				System.exit(1);
			}
		}
	}

}
