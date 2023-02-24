package no.so.mosquitto;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class LocalSubscriber implements MqttCallback {

	private MqttClient client;
	private List<String> topics;

	public LocalSubscriber(MqttClient client) {
		this.topics = new ArrayList<String>();
		this.client = client;
		client.setCallback(this);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		//connOpts.setKeepAliveInterval(30);
		
		try {
			client.connect(connOpts);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("MQTTSubclient Connected");

	}

	public void subscribe2Topic(String topic) {
		try {
			if (client.isConnected()) {
				client.subscribe(topic);
				topics.add(topic);
			}
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void unsubscribe2Topic(String topic) {
		try {
			if (topics.contains(topic) && client.isConnected()) {
				client.subscribe(topic);
				topics.remove(topic);
			}
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		System.out.println("Lost Connection to Broker on: " + client.getCurrentServerURI());
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Got new message on topic: " + topic);

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub

	}

	public static void main(String args[]) { //TODO Receive Topics list in argument
		final MosquittoBrokerClient brokerC = new MosquittoBrokerClient(Path.of("config/config_dev"),
				Path.of(("creds/credentials")), "MQTT_Mosquito_Sub_Client");
		LocalSubscriber sub = new LocalSubscriber(brokerC.getBrokerClient());
		sub.subscribe2Topic("smartocean/pd1/austevoll-nord");

	}
}