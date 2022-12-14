package no.so.broker.mqtt.hive;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck;

import no.so.utils.parser.ParseFromFile;

public class HiveBrokerClient {
	
	private final Mqtt3AsyncClient client;
	
	public HiveBrokerClient(Path config, Path credentials, String name) {
		
		Map<String,String> confs = ParseFromFile.getStrKVPairs(config,":");
		
		Map<String,String> creds = ParseFromFile.getStrKVPairs(credentials,":");
		
		String host = confs.getOrDefault("Cluster URL", "localhost");
		int port = Integer.parseInt(confs.getOrDefault("Port", "0"));
		
		String user = creds.getOrDefault("user", "user");
		String pass = creds.getOrDefault("password", "");
		
		this.client = MqttClient.builder() //TODO check need for local data persistence
		        .useMqttVersion3()
		        .identifier(name)
		        .serverHost(host)
		        .serverPort(port)
		        .useSslWithDefaultConfig()
		        .buildAsync();

		System.out.println("Configured successfully");

	}
	
	public Mqtt3AsyncClient getBrokerClient() { return this.client; }	
	
	
	private boolean authenticate() {
		
		return false;
		
	}

}
