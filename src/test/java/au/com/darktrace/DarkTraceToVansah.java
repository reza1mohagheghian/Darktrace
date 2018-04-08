package au.com.darktrace;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class DarkTraceToVansah {
	ActiveMQConnectionFactory connectionFactory;
	Connection connection;
	Session session;
	Queue queue;
	MessageConsumer consumer;
	ReadConfig configReader = new ReadConfig();
	double mediumScoreRange;
	double highScoreRange;

	public void SetMediumScoreRange(double mediumScoreRange) {
		this.mediumScoreRange = mediumScoreRange;
	}

	public void SetHighScoreRange(double highScoreRange) {
		this.highScoreRange = highScoreRange;
	}

	public void SetActiveMQHost(String host) {
		configReader.setsActiveMQHost(host);
	}

	public void SetActiveMQPort(String port) {
		configReader.setsActiveMQPort(port);
	}

	public void SetActiveMQQueueName(String queueName) {
		configReader.setsActiveMQQueue(queueName);
	}

	public void SetHTTPProxyHost(String host) {
		configReader.setsHostAddr(host);
	}

	public void SetHTTPProxyPort(String port) {
		configReader.setsPortNo(port);
	}

	public void SetToken(String token) {
		configReader.setsVQToken(token);
	}

	public void SetInstance(String instance) {
		configReader.setsVansahInstance(instance);
	}

	public void SetUp() {
		try {
			connectionFactory = new ActiveMQConnectionFactory(
					"tcp://" + configReader.getsActiveMQHost() + ":" + configReader.getsActiveMQPort());
			connection = connectionFactory.createConnection();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Consume() {
		try {
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			queue = session.createQueue(configReader.getsActiveMQQueue());
			consumer = session.createConsumer(queue);
			while (true) {
				TextMessage message = (TextMessage) consumer.receive();
				String jsonMessage = message.getText();
				// TextMessage messageCEF = (TextMessage) consumerCEF.receive();
				// String textCEF = messageCEF.getText();
				System.out.println("Received from threats queue: " + jsonMessage);
				System.out.println("-----------------------------");
				// System.out.println("Received from threatsCEF queue:
				// "+textCEF);
				JSONObject obj = new JSONObject(jsonMessage);
				String headLine = "";
				String description = "";
				String time = "";
				String name = "";
				String breachType = "";
				String modelBreachURL = "";
				String hostName = "";
				String macAddress = "";
				String vendor = "";
				String ip = "";
				String os = "";
				String typeLabel = "";
				String score = "";
				String priority = "";
				String state = "";
				String credentials = "";

				if (obj.has("time")) {
					time = String.valueOf(obj.getBigInteger("time"));
					time = time.substring(0, time.length() - 3);
					long unixSeconds = Long.valueOf(time);
					Date date = new Date(unixSeconds * 1000L);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
					sdf.setTimeZone(TimeZone.getTimeZone("GMT+10"));
					String formattedDate = sdf.format(date);
					time = formattedDate;
					System.out.println("Time: " + time);
					if (obj.has("model")) {
						name = obj.getJSONObject("model").getString("name");
						description = obj.getJSONObject("model").getString("description");
						System.out.println("Name: " + name);
						System.out.println("Description: " + description);
					}

					if (obj.has("modbreachUrl"))
						modelBreachURL = obj.getString("modbreachUrl");
					System.out.println("Model Breach: " + modelBreachURL);
					if (obj.has("device")) {
						if (obj.getJSONObject("device").has("hostname")) {
							hostName = obj.getJSONObject("device").getString("hostname");
							System.out
									.println("Device Host Name: " + obj.getJSONObject("device").getString("hostname"));
						}
					}
					if (obj.has("device")) {
						if (obj.getJSONObject("device").has("macaddress")) {
							macAddress = obj.getJSONObject("device").getString("macaddress");
							System.out.println("Device MAC Address: " + macAddress);
						}
					}
					if (obj.has("device")) {
						if (obj.getJSONObject("device").has("vendor")) {
							vendor = obj.getJSONObject("device").getString("vendor");
							System.out.println("Device Vendor Name: " + vendor);
						}
					}
					if (obj.has("device")) {
						if (obj.getJSONObject("device").has("ip")) {
							ip = obj.getJSONObject("device").getString("ip");
							System.out.println("Device IP: " + ip);
						}
					}
					if (obj.has("device")) {
						if (obj.getJSONObject("device").has("os")) {
							os = obj.getJSONObject("device").getString("os");
							System.out.println("Device Operating System: " + os);
						}
					}
					if (obj.has("device")) {
						if (obj.getJSONObject("device").has("typelabel")) {
							typeLabel = obj.getJSONObject("device").getString("typelabel");
							System.out.println("Device Operating System Type: " + typeLabel);
						}
					}
					if (obj.has("device")) {
						if (obj.getJSONObject("device").has("credentials")) {
							JSONArray credArray = obj.getJSONObject("device").getJSONArray("credentials");
							credentials = credArray.toString();
							credentials = credentials.replace("[", "");
							credentials = credentials.replace("]", "");
							credentials = credentials.replace("\"", "");
							System.out.println("Device Operating Credentials: " + credentials);
						}
					}
					double temp = 0.0;
					if (obj.has("score")) {
						temp = obj.getDouble("score");
						if (temp >= highScoreRange) {
							priority = "high";
							state = "ASSIGNED";
						} else if (temp < highScoreRange & temp >= mediumScoreRange) {
							priority = "medium";
							state = "LOGGED";
						} else if (temp < mediumScoreRange) {
							priority = "low";
							state = "LOGGED";
						} else {
							priority = "";
							state = "";
						}

						score = String.valueOf(obj.getDouble("score"));
						System.out.println("Score: " + score);
					}
					System.out.println("-----------------------------");

					if (name.contains("::")) {
						String[] split = name.split("::", 2);
						breachType = split[0];
						name = split[1];
					} else {
						throw new IllegalArgumentException("Breach Name does not contain ::");
					}

					headLine = name + " @ " + time;
					VansahRest vr = new VansahRest(jsonMessage, configReader,headLine, description, priority, breachType, name,
							modelBreachURL, hostName, macAddress, vendor, ip, os, typeLabel, score, credentials);
					vr.PostIncident();
				}
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
