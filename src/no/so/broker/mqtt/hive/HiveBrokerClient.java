package no.so.broker.mqtt.hive;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

public class HiveBrokerClient {
	
	private final Mqtt5BlockingClient client;

	private final HiveClientConfig hiveClientConfig;
	
	public HiveBrokerClient(HiveClientConfig conf) {

		this.hiveClientConfig = conf;

		this.client = Mqtt5Client.builder() //TODO config data persistence and responsetopic
		        .identifier(conf.getClient_id())
		        .serverHost(conf.getHost())
		        .serverPort(conf.getPort())
		        .buildBlocking();

		System.out.println("Broker client configured successfully");
	}

	public Mqtt5BlockingClient getBrokerClient() { return this.client; }

	public HiveClientConfig getBrokerClientConfig() {return this.hiveClientConfig;}

}
