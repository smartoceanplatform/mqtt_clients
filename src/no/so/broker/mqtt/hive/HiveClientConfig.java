package no.so.broker.mqtt.hive;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HiveClientConfig {

    private String clientId;
    private String host;
    private int port;
    private Object authentication; //TODO

    private List<Topic> topics;

    public Object getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Object authentication) {
        this.authentication = authentication;
    }

    public String getClient_id() {
        return clientId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public List<Topic> getTopics() {return this.topics;}


    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public HiveClientConfig() {


    }

    public static void main(String args[]) throws IOException {

        Constructor constructor = new Constructor(HiveClientConfig.class);
        TypeDescription configDescription = new TypeDescription(HiveClientConfig.class);
        configDescription.addPropertyParameters("topics",Topic.class);
        constructor.addTypeDescription(configDescription);
        Yaml yaml = new Yaml(constructor);

        HiveClientConfig config = yaml.load(Files.newInputStream(Path.of("config/config_dev_pub.yaml")));
    }
}
