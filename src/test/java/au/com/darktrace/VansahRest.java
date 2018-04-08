package au.com.darktrace;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.json.JSONException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class VansahRest {
	private static String URL = ".vansah.net/api/asset/incident/";
	private String VANSAH_URI = "";
	private String VANSAH_TOKEN = "";
	private String VANSAH_HEADLINE = "";
	private String VANSAH_DESCRIPTION = "";
	private String VANSAH_TYPE = "threat";
	private String VANSAH_PRIORITY = "";
	private String VANSAH_BREACHTYPE = "";
	private String VANSAH_NAME = "";
	private String VANSAH_MODELBREACHURL = "";
	private String VANSAH_HOSTNAME = "";
	private String VANSAH_MACADDRESS = "";
	private String VANSAH_VENDOR = "";
	private String VANSAH_IP = "";
	private String VANSAH_OS = "";
	private String VANSAH_TYPELABEL = "";
	private String VANSAH_SCORE = "";
	private String VANSAH_CREDENTIALS = "";
	private String JSONMESSAGE;
	HttpClientBuilder clientBuilder;
	CredentialsProvider credsProvider;

	public VansahRest(String jsonMessage, ReadConfig configReader, String vANSAH_HEADLINE, String vANSAH_DESCRIPTION, String vANSAH_PRIORITY,
			String vANSAH_BREACHTYPE, String vANSAH_NAME, String vANSAH_MODELBREACHURL, String vANSAH_HOSTNAME,
			String vANSAH_MACADDRESS, String vANSAH_VENDOR, String vANSAH_IP, String vANSAH_OS, String vANSAH_TYPELABEL,
			String vANSAH_SCORE, String vANSAH_CREDENTIALS) {
		super();
		JSONMESSAGE = jsonMessage;
		VANSAH_HEADLINE = vANSAH_HEADLINE;
		VANSAH_DESCRIPTION = vANSAH_DESCRIPTION;
		VANSAH_PRIORITY = vANSAH_PRIORITY;
		VANSAH_BREACHTYPE = vANSAH_BREACHTYPE;
		VANSAH_NAME = vANSAH_NAME;
		VANSAH_MODELBREACHURL = vANSAH_MODELBREACHURL;
		VANSAH_HOSTNAME = vANSAH_HOSTNAME;
		VANSAH_MACADDRESS = vANSAH_MACADDRESS;
		VANSAH_VENDOR = vANSAH_VENDOR;
		VANSAH_IP = vANSAH_IP;
		VANSAH_OS = vANSAH_OS;
		VANSAH_TYPELABEL = vANSAH_TYPELABEL;
		VANSAH_SCORE = vANSAH_SCORE;
		VANSAH_CREDENTIALS = vANSAH_CREDENTIALS;
		VANSAH_URI = "https://" + configReader.getsVansahInstance() + URL;
		VANSAH_TOKEN = configReader.getsVQToken();
		// "proxy2.flexirent.com" 8080
		clientBuilder = HttpClientBuilder.create();
		if (!configReader.getsHostAddr().equals("localhost")) {
			credsProvider = new BasicCredentialsProvider();
			clientBuilder.useSystemProperties();
			clientBuilder.setProxy(new HttpHost(configReader.getsHostAddr(), configReader.getsPortNo()));
			clientBuilder.setDefaultCredentialsProvider(credsProvider);
			clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
		}
		Unirest.setHttpClient(clientBuilder.build());
	}

	public void PostIncident() {

		// try {
		// HttpResponse<JsonNode> response;
		// response = Unirest.get(URL).header("TOKEN",
		// "5d820d8d07d654fc9865ca610d14b782").asJson();
		// JSONObject myObj = response.getBody().getObject();
		// System.out.println(myObj);
		// JSONObject myObj2 = (JSONObject) myObj.get("success");
		// System.out.println(myObj2.get("created_by"));
		// } catch (UnirestException e) {
		// System.out.println(e);
		// }

		try {
			HttpResponse<String> post;
			post = PostToVansah();
			if (post.getBody().contains("InvalidAccessToken")) {
				writeErrorToV_errorFile(post.getBody(), JSONMESSAGE);
				throw new JSONException("Invalid Access Token Provided!");
			} else if (post.getBody().contains("DuplicateEntry")) {
				post = PostToVansahDuplicate();
				writeLogToV_JSONFile(post.getBody(), JSONMESSAGE);
			} else if (post.getBody().contains("TypeNotFound")) {
				writeErrorToV_errorFile(post.getBody(), JSONMESSAGE);
				throw new JSONException("Wrong Parameter Name Provided!");
			} else if (post.getBody().contains("success")) {
				System.out.println("Request Posted successfully!");
				writeLogToV_JSONFile(post.getBody(), JSONMESSAGE);
			} else {
				System.out.println(post.getBody());
				writeLogToV_JSONFile(post.getBody(), JSONMESSAGE);
			}
			System.out.println("Response From Server : " + post.getStatusText());
			System.out.println("=============================");
		} catch (UnirestException e) {
			e.printStackTrace();
		}

	}

	private HttpResponse<String> PostToVansahDuplicate() throws UnirestException {
		HttpResponse<String> post;
		Random random = new Random();
		String instance = "#" + (random.nextInt(20 - 0 + 1) + 0);
		post = Unirest.post(VANSAH_URI).header("TOKEN", VANSAH_TOKEN).field("HEADLINE", VANSAH_HEADLINE + instance)
				.field("DESCRIPTION", VANSAH_DESCRIPTION).field("TYPE", VANSAH_TYPE).field("NAME", VANSAH_NAME)
				.field("BREACHTYPE", VANSAH_BREACHTYPE).field("MODELBREACHURL", VANSAH_MODELBREACHURL)
				.field("HOSTNAME", VANSAH_HOSTNAME).field("MACADDRESS", VANSAH_MACADDRESS)
				.field("VENDOR", VANSAH_VENDOR).field("IPADDRESS", VANSAH_IP).field("OSNAME", VANSAH_OS)
				.field("TYPELABEL", VANSAH_TYPELABEL).field("SCORE", VANSAH_SCORE).field("PRIORITY", VANSAH_PRIORITY)
				.field("CREDENTIALS", VANSAH_CREDENTIALS).asString();
		return post;
	}

	private HttpResponse<String> PostToVansah() throws UnirestException {
		HttpResponse<String> post;
		post = Unirest.post(VANSAH_URI).header("TOKEN", VANSAH_TOKEN).field("HEADLINE", VANSAH_HEADLINE)
				.field("DESCRIPTION", VANSAH_DESCRIPTION).field("TYPE", VANSAH_TYPE).field("NAME", VANSAH_NAME)
				.field("BREACHTYPE", VANSAH_BREACHTYPE).field("MODELBREACHURL", VANSAH_MODELBREACHURL)
				.field("HOSTNAME", VANSAH_HOSTNAME).field("MACADDRESS", VANSAH_MACADDRESS)
				.field("VENDOR", VANSAH_VENDOR).field("IPADDRESS", VANSAH_IP).field("OSNAME", VANSAH_OS)
				.field("TYPELABEL", VANSAH_TYPELABEL).field("SCORE", VANSAH_SCORE).field("PRIORITY", VANSAH_PRIORITY)
				.field("CREDENTIALS", VANSAH_CREDENTIALS).asString();
		return post;
	}

	private static void writeErrorToV_errorFile(String serverResponse, String jsonMessage) {
		String errorFile = "Vansah\\V_error.txt";
		File file;
		BufferedWriter bWriter;
		file = new File(errorFile);

		// Create the file
		try {
			if (file.createNewFile()) {
				// System.out.println("File is created!");
			} else {
				// System.out.println("File already exists.");
			}
			FileWriter writer = new FileWriter(file, true);
			bWriter = new BufferedWriter(writer);
			bWriter.write(serverResponse);
			bWriter.newLine();
			bWriter.write(jsonMessage);
			bWriter.newLine();
			bWriter.write("========================");
			bWriter.newLine();
			bWriter.close();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeLogToV_JSONFile(String serverResponse, String jsonMessage) {
		String logFile = "Vansah\\V_JSON.txt";
		File file;
		BufferedWriter bWriter;
		file = new File(logFile);

		// Create the file
		try {
			if (file.createNewFile()) {
				// System.out.println("File is created!");
			} else {
				// System.out.println("File already exists.");
			}
			FileWriter writer = new FileWriter(file, true);
			bWriter = new BufferedWriter(writer);
			bWriter.write(serverResponse);
			bWriter.newLine();
			bWriter.write(jsonMessage);
			bWriter.newLine();
			bWriter.write("========================");
			bWriter.newLine();
			bWriter.close();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
