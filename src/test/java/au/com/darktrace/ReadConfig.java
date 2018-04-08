package au.com.darktrace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ReadConfig {

	private String sMaxLogResponse;
	private String sVansahConnectionType;
	// private String sVSAMToken;
	private String sRegScreenShotsDirectory;
	private String sVansahInstance;
	private String sUpdateVansah;
	private String sDevMode;
	private String sAgentName;
	private String sVQToken;
	private String sHostAddr;
	private String sPortNo;
	private String sActiveMQHost;
	private String sActiveMQPort;
	private String sActiveMQQueue;
	private String CONFIG = "Vansah\\config.vns";
	Properties VNSProperties;
	ConfigSetup configuration;
	InputStream reader = null;
	OutputStream writer = null;

	// Start Constructor
	public ReadConfig() {
		VNSProperties = new Properties();
		try {

			configuration = new ConfigSetup();
			reader = new FileInputStream(CONFIG);
			VNSProperties.load(reader);

			sMaxLogResponse = VNSProperties.getProperty("sMaxLogResponse");
			sVansahConnectionType = VNSProperties.getProperty("sVansahConnectionType");
			// sVSAMToken = VNSProperties.getProperty("sVSAMToken");
			sRegScreenShotsDirectory = VNSProperties.getProperty("sRegScreenShotsDirectory");
			sVansahInstance = VNSProperties.getProperty("sVansahInstance");
			sUpdateVansah = VNSProperties.getProperty("sUpdateVansah");
			sDevMode = VNSProperties.getProperty("sDevMode");
			sAgentName = VNSProperties.getProperty("sAgentName");
			sVQToken = VNSProperties.getProperty("sVQToken");
			sHostAddr = VNSProperties.getProperty("sHostAddr");
			sPortNo = VNSProperties.getProperty("sPortNo");
			sActiveMQHost = VNSProperties.getProperty("sActiveMQHost");
			sActiveMQPort = VNSProperties.getProperty("sActiveMQPort");
			sActiveMQQueue=VNSProperties.getProperty("sActiveMQQueue");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	// End Constructor

	/*
	 * Functions to get info from Config file
	 */

	public void initialiseAgent(String propertyName, String value) {
		try {

			VNSProperties.setProperty(propertyName, value);
			File f = new File(CONFIG);
			writer = new FileOutputStream(f);
			VNSProperties.store(writer, "[Settings]");

			writer.close();
			reader = new FileInputStream(CONFIG);
			VNSProperties.load(reader);
			reader.close();

			sMaxLogResponse = VNSProperties.getProperty("sMaxLogResponse");
			sVansahConnectionType = VNSProperties.getProperty("sVansahConnectionType");
			// sVSAMToken = VNSProperties.getProperty("sVSAMToken");
			sRegScreenShotsDirectory = VNSProperties.getProperty("sRegScreenShotsDirectory");
			sVansahInstance = VNSProperties.getProperty("sVansahInstance");
			sUpdateVansah = VNSProperties.getProperty("sUpdateVansah");
			sDevMode = VNSProperties.getProperty("sDevMode");
			sAgentName = VNSProperties.getProperty("sAgentName");
			sVQToken = VNSProperties.getProperty("sVQToken");
			sHostAddr = VNSProperties.getProperty("sHostAddr");
			sPortNo = VNSProperties.getProperty("sPortNo");
			sActiveMQHost = VNSProperties.getProperty("sActiveMQHost");
			sActiveMQPort = VNSProperties.getProperty("sActiveMQPort");
			sActiveMQQueue=VNSProperties.getProperty("sActiveMQQueue");

		} catch (IOException ex) {

			ex.printStackTrace();
		} finally {

			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
					System.out.println("Unable to close Writer!!!");
				}
			}

		}
	}

	/*
	 * All Getters Methods for Vansah Properties.
	 * 
	 */

	public String getsAgentName() {
		return sAgentName;
	}

	public String getsVQToken() {
		return sVQToken;
	}

	public String getsVansahInstance() {
		return sVansahInstance;
	}

	// public String getsVSAMToken() {
	// return sVSAMToken;
	// }

	public String getsUpdateVansah() {
		return sUpdateVansah;
	}

	public String getsRegScreenShotsDirectory() {
		return sRegScreenShotsDirectory;
	}

	public String getsMaxLogResponse() {
		return sMaxLogResponse;
	}

	public String getsVansahConnectionType() {
		return sVansahConnectionType;
	}

	public String getsDevMode() {
		return sDevMode;
	}

	public String getsHostAddr() {
		return sHostAddr;
	}

	public int getsPortNo() {
		return Integer.valueOf(sPortNo);
	}

	public String getsActiveMQHost() {
		return sActiveMQHost;
	}
	public String getsActiveMQQueue() {
		return sActiveMQQueue;
	}
	public void setsVansahInstance(String sVansahInstance) {
		this.sVansahInstance = sVansahInstance;
	}

	public void setsVQToken(String sVQToken) {
		this.sVQToken = sVQToken;
	}

	public void setsHostAddr(String sHostAddr) {
		this.sHostAddr = sHostAddr;
	}

	public void setsPortNo(String sPortNo) {
		this.sPortNo = sPortNo;
	}

	public void setsActiveMQHost(String sActiveMQHost) {
		this.sActiveMQHost = sActiveMQHost;
	}

	public void setsActiveMQPort(String sActiveMQPort) {
		this.sActiveMQPort = sActiveMQPort;
	}

	public void setsActiveMQQueue(String sActiveMQQueue) {
		this.sActiveMQQueue = sActiveMQQueue;
	}

	public int getsActiveMQPort() {
		return Integer.valueOf(sActiveMQPort);
	}

	/*
	 * End Getters Region.
	 */

	/*
	 * public static void main(String args[]){
	 * 
	 * ReadConfig read = new ReadConfig();
	 * 
	 * 
	 * System.out.println("Screen Register before : "+
	 * read.getsRegScreenShotsDirectory());
	 * read.initialiseAgent("sRegScreenShotsDirectory", "D:\\pics\\Home");
	 * System.out.println("Screen Register after : "+
	 * read.getsRegScreenShotsDirectory());
	 * 
	 * }
	 */
}
