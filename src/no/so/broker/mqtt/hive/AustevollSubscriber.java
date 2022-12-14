package no.so.broker.mqtt.hive;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AustevollSubscriber {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// "ocean-monitoring-models"
		/*
		 * 		// set a callback that is called when a message is received (using the async API
		// style)
		hiveclient.getBrokerClient().toAsync().publishes(ALL, publish -> {
			System.out.println(
					"Received message: " + publish.getTopic() + " -> " + UTF_8.decode(publish.getPayload().get()));

			// disconnect the client after a message was received
			hiveclient.getBrokerClient().disconnect();
		});
		 */

	}

}
