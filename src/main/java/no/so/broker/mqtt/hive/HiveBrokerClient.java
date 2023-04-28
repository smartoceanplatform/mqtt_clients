package no.so.broker.mqtt.hive;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

public class HiveBrokerClient {
	
	private final Mqtt5BlockingClient client;

	private final BrokerConfig clientConfig;
	
	public HiveBrokerClient(BrokerConfig conf) {

		this.clientConfig = conf;

		this.client = conf.getAuthentication().hasCredentials() ?
				Mqtt5Client.builder()
				.identifier(conf.getClientId())
				.serverHost(conf.getHost())
				.serverPort(conf.getPort())
				.sslWithDefaultConfig()
				.buildBlocking() :
				Mqtt5Client.builder()
				.identifier(conf.getClientId())
				.serverHost(conf.getHost())
				.serverPort(conf.getPort())
				.buildBlocking();

		System.out.println("Broker client configured successfully");
	}

	public Mqtt5BlockingClient getBrokerClient() { return this.client; }

	public BrokerConfig getBrokerClientConfig() {return this.clientConfig;}

}
