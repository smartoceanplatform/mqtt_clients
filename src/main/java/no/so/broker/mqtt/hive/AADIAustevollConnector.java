package no.so.broker.mqtt.hive;

import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AADIAustevollConnector {
    public static void main(String[] args) throws Exception {

        Path config = args.length > 0 ? Path.of(args[0]): Path.of("config/config_prod.yaml");

        if(Files.isReadable(config)) {
            // Load configs form YAML
            final ClientConfig conf = ClientConfig.loadFromFile(config);

            final HiveBrokerClient adapterClient = new HiveBrokerClient(conf.getSourceBroker());

            // create an MQTT client to get data from Austevoll
            final Mqtt5ConnAck adapterAck = conf.getSourceBroker().getAuthentication().hasCredentials() ?
                    adapterClient.getBrokerClient().connectWith()
                            .simpleAuth()
                            .username(conf.getSourceBroker().getAuthentication().getUsername())
                            .password(UTF_8.encode(conf.getSourceBroker().getAuthentication().getPassword()))
                            .applySimpleAuth()
                            .send() :
                    adapterClient.getBrokerClient().connect();


            if (!adapterAck.getReasonCode().isError()) {

                System.out.println("Subscriber connected successfully");

                System.out.println("Subscribing to source topics");
                for (Topic topic : conf.getSourceBroker().getTopics()) {
                    adapterClient.getBrokerClient().subscribeWith()
                            .topicFilter(topic.getTopic())
                            .send();
                }

                // create an MQTT client
                final HiveBrokerClient hiveclient = new HiveBrokerClient(conf.getBroker());

                // set a callback that is called when a message is received (using the async API style)
                adapterClient.getBrokerClient().toAsync().publishes(ALL, publish -> {
                    System.out.println("Received message: " +
                            publish.getTopic() + " -> " +
                            UTF_8.decode(publish.getPayload().get()));

                    System.out.println("Trying to connect to broker to publish...");
                    // connect to HiveMQ Cloud with TLS and username/pw
                    Mqtt5ConnAck connAck = conf.getBroker().getAuthentication().hasCredentials() ?
                            hiveclient.getBrokerClient().connectWith()
                                    .simpleAuth()
                                    .username(conf.getBroker().getAuthentication().getUsername())
                                    .password(UTF_8.encode(conf.getBroker().getAuthentication().getPassword()))
                                    .applySimpleAuth()
                                    .send() :
                            hiveclient.getBrokerClient().connect();

                    if (!connAck.getReasonCode().isError()) {
                        hiveclient.getBrokerClient()
                                .toBlocking()
                                .publishWith()
                                .topic(conf.getBroker().getTopics().get(0).getTopic())
                                .qos(conf.getBroker().getTopics().get(0).getConfiguredQos())
                                .payload(publish.getPayload().get())
                                .send();
                        System.out.println("Published data file ");
                        hiveclient.getBrokerClient().disconnect();
                    }
                });
            }
            else{
                System.out.println("Error connecting to data source broker: "+adapterAck.getReasonString());
            }
        }
    }
}
