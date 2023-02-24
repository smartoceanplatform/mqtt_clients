package main.java.no.so.broker.mqtt.mosquitto;

import java.nio.file.Path;
import java.util.Map;

import main.java.no.so.utils.parser.ParseFromFile;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MosquittoBrokerClient {
	
	private MqttClient client;
	
	public MosquittoBrokerClient(Path config, Path credentials, String clientName) {
		
		
		
		Map<String,String> confs = ParseFromFile.getStrKVPairs(config,":");
		Map<String,String> creds = ParseFromFile.getStrKVPairs(credentials,":");
		
		String host = confs.getOrDefault("Cluster URL", "tcp://localhost"); //tcp://localhost:PORT or local://localhost or ssl://localhost
		
		int port = Integer.parseInt(confs.getOrDefault("Port", "1883"));
		
		StringBuilder sb = new StringBuilder();
		String broker = sb.append(host).append(':').append(port).toString();
		
		String user = creds.getOrDefault("user", "user");
		String pass = creds.getOrDefault("password", "");
		
		//TODO use credentials (SSL connection)
		
		
		System.out.println("MQTTClient Configuring broker client for: "+broker);
		
		try {
			client = new MqttClient(broker, clientName); //TODO check need for local data persistence
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Client connection setup done.");

	}
	
	public MqttClient getBrokerClient() { return this.client; }	

}
