package no.so.broker.mqtt.hive;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class AADIVirtualPublisher {
	final static short minutes_interval = 3;


	public static void main(String[] args) throws Exception {

		Path config = args.length > 0? Path.of(args[0]): Path.of("config/config.yaml");

		if(Files.isReadable(config)){
			final int seconds = minutes_interval * 60;

			Constructor constructor = new Constructor(HiveClientConfig.class);//Car.class is root
			TypeDescription configDescription = new TypeDescription(HiveClientConfig.class);
			configDescription.addPropertyParameters("Topics",Topic.class);
			constructor.addTypeDescription(configDescription);
			Yaml yaml = new Yaml(constructor);

			// Load configs form YAML
			final HiveClientConfig conf = yaml.load(Files.newInputStream(config));

			// create an MQTT client
			final HiveBrokerClient hiveclient = new HiveBrokerClient(conf);

			// publish a message to the topics in config yaml
			final Path path = Path.of(conf.getTopics().get(0).source); //TODO change this

			Files.list(path).forEach(xml ->
			{
				try {
					System.out.println("Trying to connect to broker...");
					Mqtt5ConnAck connAck = hiveclient.getBrokerClient().connect();

					if(!connAck.getReasonCode().isError()) {
						hiveclient.getBrokerClient()
								.toBlocking()
								.publishWith()
								.topic(conf.getTopics().get(0).publishTopic)
								.qos(conf.getTopics().get(0)._getQos())
								.payload(UTF_8.encode(Files.readString(xml))) //TODO parameter
								.send();
						System.out.println("Published data file: " + xml.toAbsolutePath().toString());
						hiveclient.getBrokerClient().disconnect();
						System.out.println("Disconnected");
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
