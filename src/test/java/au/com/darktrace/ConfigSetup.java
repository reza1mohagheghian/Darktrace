package au.com.darktrace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigSetup {

	File theDir;
	Properties VNSProperties;
	InputStream reader;
	FileOutputStream output;
	String file = "Vansah\\config.vns";

	public ConfigSetup() {
		setUp();
	}
	// Function which creates Vansah Folder in the Project root directory.
	// Then it created config.vns (Vansah configuration file") if it does not
	// exist.

	private void setUp() {
		BufferedReader br = null;
		theDir = new File(file);
		// Check if the directory and file exists
		if (!theDir.exists()) {
			theDir.getParentFile().mkdirs();
			try {
				FileWriter writer = new FileWriter(theDir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writeToConfig();

		} else {

			try {
				// Initialize the buffer reader to read the file.
				br = new BufferedReader(new FileReader(file));
				// Check if the file is empty. if yes then write property to
				// file.
				if (br.readLine() == null) {
					writeToConfig();
				}
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * Method to write to configuration file once created. It does not change if
	 * the file is already present if folder structure Vansah/config.vns
	 */

	private void writeToConfig() {
		VNSProperties = new Properties();

		try {
			reader = new FileInputStream(file);
			VNSProperties.load(reader);

			VNSProperties.setProperty("sDevMode", "1");
			VNSProperties.setProperty("sVansahConnectionType", "1");
			// VNSProperties.setProperty("sVSAMToken", "FLEXG2015");
			VNSProperties.setProperty("sVQToken", "");
			VNSProperties.setProperty("sVansahInstance", "");
			VNSProperties.setProperty("sMaxLogResponse", "120");
			VNSProperties.setProperty("sUpdateVansah", "0");
			VNSProperties.setProperty("sAgentName", "");
			VNSProperties.setProperty("sRegScreenShotsDirectory", "");
			VNSProperties.setProperty("sHostAddr", "localhost");
			VNSProperties.setProperty("sPortNo", "0");
			VNSProperties.setProperty("sActiveMQHost", "127.0.0.1");
			VNSProperties.setProperty("sActiveMQPort", "61616");
			VNSProperties.setProperty("sActiveMQQueue", "threats");

		} catch (IOException ex) {

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		try {
			output = new FileOutputStream(theDir);
			VNSProperties.store(output, "[Settings]");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public static void main(String args[]) {
		ConfigSetup con = new ConfigSetup();
		con.setUp();
	}

}
