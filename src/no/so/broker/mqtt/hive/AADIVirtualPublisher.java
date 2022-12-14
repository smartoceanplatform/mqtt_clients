package no.so.broker.mqtt.hive;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck;

public class AADIVirtualPublisher {
	final static Path path = Path.of("/home/keila/smartoceanplatform/smartocean-messaging/samplesensordata/SFI_Austevoll_NordDestination"); //TODO change this
	final static short minutes_interval = 10;


	public static void main(String[] args) throws Exception {

		final int seconds = minutes_interval * 60;
		// create an MQTT client
		final HiveBrokerClient hiveclient = new HiveBrokerClient(Path.of("config/config_dev"), Path.of(("creds/credentials")), "virtualProvider");


		// publish a message to the topic "my/test/topic"
		Files.list(path).forEach(xml -> 
			{
				try {
					System.out.println("Trying to connect with broker...");
					CompletableFuture<Mqtt3ConnAck> connAck = new CompletableFuture<Mqtt3ConnAck>();
					connAck = hiveclient.getBrokerClient().connect();
					hiveclient.getBrokerClient().publishWith().topic("smartocean/pd1/austevoll-nord").payload(UTF_8.encode(Files.readString(xml))).send();
					System.out.println("Publishing data file: "+xml.toAbsolutePath().toString());
					hiveclient.getBrokerClient().disconnect();
					System.out.println("Disconnected");
					Thread.sleep(1000*seconds);
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("Error publing data file: "+xml.toAbsolutePath().toString());
				}
			});
		}
}
