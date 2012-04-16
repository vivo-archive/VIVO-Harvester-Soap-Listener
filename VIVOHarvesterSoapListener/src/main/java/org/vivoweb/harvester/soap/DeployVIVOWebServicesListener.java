package org.vivoweb.harvester.soap;

import java.io.File; 
import java.io.IOException;
import org.jSoapServer.SoapServer;
import org.vivoweb.harvester.soap.VIVOWebServicesListener;
import org.quickserver.util.xmlreader.QSAdminServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vivoweb.harvester.util.InitLog;
import org.vivoweb.harvester.util.args.ArgDef;
import org.vivoweb.harvester.util.args.ArgList;
import org.vivoweb.harvester.util.args.ArgParser;
import org.vivoweb.harvester.util.args.UsageException;

public class DeployVIVOWebServicesListener {

	/**
	 * SLF4J Logger
	 */
	private static Logger log = LoggerFactory.getLogger(DeployVIVOWebServicesListener.class);

	/**
	 * This will hold the value of the type of encryption that will be used
	 */
	private String decryption;
	
	/**
	 * This will hold the folder path that incoming XML messages will be saved into
	 */
	private String folderPath;
	
	/**
	 * This will hold the path to the jSoapServer config file
	 */
	private String soapConfigPath;
	
	/**
	 * This will hold the published name of the service in question
	 */
	private String serviceName;
	
	/**
	 * This will hold the name of schema file that incoming XML messages will be validated against
	 */
	private String schemaFile;
	
	/**
	 * Command line Constructor
	 * @param args commandline arguments
	 * @throws IOException error creating task
	 * @throws UsageException user requested usage message
	 */
	private DeployVIVOWebServicesListener(String[] args) throws IOException, UsageException {
		this(getParser().parse(args));
	}
	
	/**
	 * Arglist Constructor
	 * @param args option set of parsed args
	 * @throws IOException error creating task
	 */
	private DeployVIVOWebServicesListener(ArgList args) throws IOException {
		this(
			args.get("d"), args.get("o"), args.get("c"), args.get("s"), args.get("x")
		);
	}
	/**
	 * Library style Constructor
	 */
	public DeployVIVOWebServicesListener(String decryption, String folderPath, String soapConfigPath, String serviceName, String schemaFile){
		
		this.decryption = decryption;
		this.folderPath = folderPath;
		this.soapConfigPath = soapConfigPath;
		this.serviceName = serviceName;
		System.out.println(serviceName);
		this.schemaFile = schemaFile;
		
		// Create a file object to test the schemaFile with
		File testSchema = new File(this.schemaFile);
		
		// Check to see if the schemaFile a) is a file, b) can be read, and c) exists 
		if ( ! testSchema.isFile() || ! testSchema.canRead() || ! testSchema.exists() ) {
			// The schemaFile is not ready for us, so we need to throw an exception
			throw new IllegalArgumentException("Schema file is either not a file, does not exist, or cannot be read from!");
		}

		// Create a file object to test the output folderPath with
		File testFolderPath = new File(this.folderPath);
		
		// Check to see if the output folderPath a) is a directory, b) can be written, and c) exists
		if ( ! testFolderPath.isDirectory() || ! testFolderPath.canWrite() || ! testFolderPath.exists() ) {
			// The output folderPath is not ready for us, so we need to throw an exception
			throw new IllegalArgumentException("Output folder path is either not a directory, does not exist, or cannot be written to!");
		}
		
		WebServerSingleton.getInstance();
		// In this state, no one will now until someone calls the service to pass information
		WebServerSingleton.setProperty("schemaFile", this.schemaFile);
		WebServerSingleton.setProperty("folderPath", this.folderPath);
	}
	
	/**
	 * Executes the task
	 * @throws IOException error processing record handler
	 */
	public void execute() throws IOException {
		try {
			// creating a new soap server
			SoapServer vivoSoapServer = new SoapServer();
			
			
			//TODO clean me (messy hard to read etc)
		    if (vivoSoapServer.initService(this.soapConfigPath)) {
		    	vivoSoapServer.deployRpcSoapService(org.vivoweb.harvester.soap.VIVOWebServicesListener.class,this.serviceName);
      			vivoSoapServer.startServer();
				QSAdminServerConfig adminConfig = vivoSoapServer.getConfig().getQSAdminServerConfig();
				if (adminConfig != null) {
					vivoSoapServer.startQSAdminServer();
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
			new DeployVIVOWebServicesListener(args).execute();
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
