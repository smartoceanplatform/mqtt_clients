package main.java.no.so.broker.mqtt.hive;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;

public class AADIVirtualPublisher {

	public static void main(String[] args) throws Exception {
		final short minutes_interval = 3;
		final int seconds = minutes_interval * 60;

		Path config = args.length > 0? Path.of(args[0]): Path.of("config/config.yaml");

		if(Files.isReadable(config)){

			// Load configs form YAML
			final ClientConfig conf = ClientConfig.loadFromFile(config);

			// create an MQTT client
			final HiveBrokerClient hiveclient = new HiveBrokerClient(conf);

			// publish a message to the topics in config yaml
			final Path path = Path.of(conf.getTopics().get(0).getSource()); //TODO change this

			Files.list(path).forEach(xml ->
			{
				try {
					System.out.println("Trying to connect to broker...");
					Mqtt5ConnAck connAck = hiveclient.getBrokerClient().connect();

					if(!connAck.getReasonCode().isError()) {
						hiveclient.getBrokerClient()
								.toBlocking()
								.publishWith()
								.topic(conf.getTopics().get(0).getPublishTopic())
								.qos(conf.getTopics().get(0).getConfiguredQos())
								.payload(UTF_8.encode(Files.readString(xml)))
								.send();
						System.out.println("Published data file: " + xml.toAbsolutePath().toString());
						Thread.sleep(1000 * seconds);
					}
					else{
						System.out.println("Error connecting to Broker");
						//TODO deal with type of error to try reconnect
					}
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
					System.err.println("Error publishing data file: "+xml.toAbsolutePath().toString());
				}
				finally {
					hiveclient.getBrokerClient().disconnect();
					System.out.println("Disconnected");
				}
			});
		}

		else if(args.length < 1 ) {
			System.err.println("Please provide a valid path to the configuration file.");
			System.exit(1);
		}


		}
}
