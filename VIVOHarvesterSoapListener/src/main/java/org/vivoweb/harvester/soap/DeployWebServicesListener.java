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

public class SoapListener {

	/**
	 * SLF4J Logger
	 */
	private static Logger log = LoggerFactory.getLogger(SoapListener.class);
	/**
	 * RecordHandler to put data in.
	 */
	private RecordHandler outputRH;
	
	
	/**
	 * Command line Constructor
	 * @param args commandline arguments
	 * @throws IOException error creating task
	 * @throws UsageException user requested usage message
	 */
	private SoapListener(String[] args) throws IOException, UsageException {
		this(getParser().parse(args));
	}
	
	/**
	 * Arglist Constructor
	 * @param args option set of parsed args
	 * @throws IOException error creating task
	 */
	private SoapListener(ArgList args) throws IOException {
		String decryption = args.get("d");
		String folderPath = args.get("o");
		String soapConfigPath = args.get("c");
		String serviceName = args.get("s");
		init(decryption, folderPath, soapConfigPath, serviceName);
	}
	/**
	 * Library style Constructor
	 */
	public SoapListener(String decryption, String folderPath, String soapConfigPath, String serviceName){
		init(decryption, folderPath, soapConfigPath, serviceName);
	}
	/**
	 * The initializing method called on via the constructors.
	 */
	private void init(String decryption, String folderPath, String soapConfigPath, String serviceName){
		
	}
	
	/**
	 * Executes the task
	 * @throws IOException error processing record handler
	 */
	public void execute() throws IOException {
		try {
			// creating a new soap server
		
			SoapServer myServer = new SoapServer();
			// configuring the server properties
		    String confFile =  "jSoapServer.xml";           
			
		    if (myServer.initService(confFile)) {
				myServer.deployRpcSoapService(org.jSoapServer.WebServicesListener.class, "PeopleListener");
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
	
	/**
	 * Get the ArgParser for this task
	 * @return the ArgParser
	 */
	private static ArgParser getParser() {
		ArgParser parser = new ArgParser("SoapListener");
		parser.addArgument(new ArgDef().setShortOption('d').setLongOpt("decrypt").withParameter(true, "DECRYPT").setDescription("The SEARCHMESSAGE file path.").setRequired(true));
		parser.addArgument(new ArgDef().setShortOption('c').setLongOpt("configFile").withParameter(true, "CONFIG").setDescription("The location of jSoapServer config file").setRequired(true));
		parser.addArgument(new ArgDef().setShortOption('o').setLongOpt("output").withParameter(true, "OUTPUT_FOLDER").setDescription("result file folder path").setRequired(true));
		parser.addArgument(new ArgDef().setShortOption('s').setLongOpt("sericeName").withParameter(true, "SERVICE_NAME").setDescription("The name of the published web service").setRequired(true));
		parser.addArgument(new ArgDef().setShortOption('O').setLongOpt("outputOverride").withParameterValueMap("RH_PARAM", "VALUE").setDescription("override the RH_PARAM of output recordhandler using VALUE").setRequired(false));
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
			new SoapListener(args).execute();
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
