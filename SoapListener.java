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
		String port = args.get("p");
		String myIP = args.get("l");
		String decryption = args.get("d");
		String folderPath = args.get("o");
		String[] acceptedIP = (String []) args.getAll("v").toArray();
		init(port, myIP,acceptedIP, decryption, folderPath);
	}
	/**
	 * Library style Constructor
	 */
	public SoapListener(String port, String myIP, String[] acceptedIP, String decryption, String folderPath){
		init(port, myIP,acceptedIP, decryption, folderPath);
	}
	/**
	 * The initializing method called on via the constructors.
	 */
	private void init(String port, String myIP, String[] acceptedIP, String decryption, String folderPath){
		
	}
	
	/**
	 * Executes the task
	 * @throws IOException error processing record handler
	 */
	public void execute() throws IOException {
	
	}
	
	/**
	 * Get the ArgParser for this task
	 * @return the ArgParser
	 */
	private static ArgParser getParser() {
		ArgParser parser = new ArgParser("SoapListener");
		parser.addArgument(new ArgDef().setShortOption('l').setLongOpt("localIp").withParameter(true, "IP").setDescription("The IP which the program is listening from.").setRequired(false));
		parser.addArgument(new ArgDef().setShortOption('v').setLongOpt("validIp").withParameterValueMap("VALIDIP", "VALUE").setDescription("A list of VALID IPs which will send messages to this program.").setRequired(false));
		parser.addArgument(new ArgDef().setShortOption('d').setLongOpt("decrypt").withParameter(true, "DECRYPT").setDescription("The SEARCHMESSAGE file path.").setRequired(true));
		parser.addArgument(new ArgDef().setShortOption('p').setLongOpt("port").withParameter(true, "PORT").setDescription("The port the listener will be listening to").setRequired(true));
		parser.addArgument(new ArgDef().setShortOption('o').setLongOpt("output").withParameter(true, "OUTPUT_FOLDER").setDescription("result file folder path").setRequired(true));
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
