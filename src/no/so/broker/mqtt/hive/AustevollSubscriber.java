package no.so.broker.mqtt.hive;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;

public class AustevollSubscriber {

	public static void main(String[] args) {

		Path config = args.length > 0 ? Path.of(args[0]) : Path.of("config/config");

		if (Files.isReadable(config)) {

			try {
				Constructor constructor = new Constructor(ClientConfig.class);//Car.class is root
				TypeDescription configDescription = new TypeDescription(ClientConfig.class);
				configDescription.addPropertyParameters("Topics", Topic.class);
				constructor.addTypeDescription(configDescription);
				Yaml yaml = new Yaml(constructor);

				// Load configs form YAML
				final ClientConfig conf = yaml.load(Files.newInputStream(Path.of("config/config_dev_pub.yaml")));

				// create an MQTT client
				final HiveBrokerClient hiveclient = new HiveBrokerClient(conf);

				hiveclient.getBrokerClient().toAsync().connect().thenAccept(ack -> AustevollSubscriber.logBrokerConnection(ack));

				hiveclient.getBrokerClient()
						.toAsync()
						.subscribeWith()
						.topicFilter("smartocean/pd1/austevoll-nord") //TODO parameter
						.topicFilter("smartocean/pd1/austevoll-sor") //TODO parameter
						.qos(MqttQos.AT_LEAST_ONCE)
						.send();

				//set a callback that is called when a message is received (using the async API style)
				hiveclient.getBrokerClient().toAsync().publishes(ALL, new AADIXMLCallback()::accept,
						Executors.newSingleThreadExecutor()); //validates each new message in a new thread
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void logBrokerConnection(Mqtt5ConnAck connAck){
		System.out.println("Connected to Broker "+connAck); //TODO observability - Log
	}

}
