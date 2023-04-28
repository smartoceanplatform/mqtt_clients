package no.so.broker.mqtt.hive;

import java.util.List;

public class BrokerConfig {
    private String clientId;
    private String host;
    private int port;
    private Authentication authentication; //TODO change to token
    private List<Topic> topics;

    public Authentication getAuthentication() {
        return this.authentication;
    }

    public void setAuthentication(Authentication auth){
        this.authentication = auth;
    }

    public String getClientId() {
        return clientId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public List<Topic> getTopics() {return this.topics;}

    protected void setClientId(String clientId) {
        this.clientId = clientId;
    }

    protected void setHost(String host) {
        this.host = host;
    }

    protected void setPort(int port) {
        this.port = port;
    }

    private void setTopics(List<Topic> topics) {
        this.topics = topics;
    }
}
