package au.com.darktrace;

import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.testng.annotations.Test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TestRest {
	HttpClientBuilder clientBuilder;
	CredentialsProvider credsProvider;

	@Test
	public void testrest() throws UnirestException {
		clientBuilder = HttpClientBuilder.create();
		Unirest.setHttpClient(clientBuilder.build());
		HttpResponse<String> post;
		post = Unirest.post("https://testpoint.vansah.net/api/asset/incident/")
				.header("TOKEN", "eed15e2b1b55dd2fa40615c325ca76d0").field("HEADLINE", "test1")
				.field("DESCRIPTION", "test").field("TYPE", "threat").field("NAME", "test")
				.field("BREACHTYPE", "test").field("MODELBREACHURL", "test")
				.field("HOSTNAME", "test").field("MACADDRESS", "test")
				.field("VENDOR", "test").field("IPADDRESS", "test").field("OSNAME", "test")
				.field("TYPELABEL", "test").field("SCORE", "test")
//				.field("PROJECTKEY", "test")
				.field("PRIORITY", "test")
//				.field("STATE", "test")
				.field("CREDENTIALS", "test")
//				.field("ASSIGNED_TO", "test")
//				.field("SUBSYSTEM", "test").field("ENVIRONMENT", "test")
				.asString();
		System.out.println(post.getHeaders());
		System.out.println(post.getBody());
		System.out.println(post.getStatusText());

	}
}
