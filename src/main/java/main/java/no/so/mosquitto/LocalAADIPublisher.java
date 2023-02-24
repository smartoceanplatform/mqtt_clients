package main.java.no.so.broker.mqtt.mosquitto;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class LocalAADIPublisher implements MqttCallback {

	final static int qos = 0; // best effort delivery //TODO review this
	final static Path path = Path
			.of("/home/keila/smartoceanplatform/smartocean-messaging/samplesensordata/SFI_Austevoll_NordDestination"); // TODO
																														// change
																														// this
	final static short minutes_interval = 15;

	public LocalAADIPublisher() {

	}

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("Message successfully delivered!");

	}

	public static void main(String[] args){

		final int seconds = minutes_interval * 60;
		// create an MQTT client
		final MosquittoBrokerClient brokerC = new MosquittoBrokerClient(Path.of("config/config_dev"),
				Path.of(("creds/credentials")), "MQTT_Mosquito_Sub_Client");
		
		brokerC.getBrokerClient().setCallback(new LocalAADIPublisher());
		
		System.out.println("Trying to connect to broker...");

		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		
		try {
			brokerC.getBrokerClient().connect(connOpts);
			
			System.out.println("Connected to broker.");

			// publish a message to the topic "my/test/topic"
			Files.list(path).forEach(xml -> {
				try {

					System.out.println("Publishing data file: " + xml.toAbsolutePath().toString());
					brokerC.getBrokerClient().publish("smartocean/pd1/austevoll-nord",
							UTF_8.encode(Files.readString(xml)).array(), qos, false);
					
					Thread.sleep(1000 * seconds);
				} catch (IOException | InterruptedException | MqttException e) {
					// TODO Auto-generated catch block
					System.err.println("Error publing data file: " + xml.toAbsolutePath().toString());
					e.printStackTrace();
				}
			});
			
			brokerC.getBrokerClient().disconnect();
			System.out.println("Disconnected from broker.");
			
		} catch (MqttException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
