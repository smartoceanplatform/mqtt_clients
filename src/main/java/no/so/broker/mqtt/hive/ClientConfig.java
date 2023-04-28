package no.so.broker.mqtt.hive;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Class automatically factored by yaml parser constructor
public class ClientConfig {

    private static ClientConfig instance = null; //singleton

    private BrokerConfig broker;

    // The source of data can be either a source folder or a subscribed topic from a broker
    private String sourceFolder;
    private int delay;

    private BrokerConfig sourceBroker;


    public ClientConfig() {

    }

    public BrokerConfig getBroker() {
        return this.broker;
    }

    public void setBroker(BrokerConfig broker) {
        this.broker = broker;
    }

    public BrokerConfig getSourceBroker() {
        return this.sourceBroker;
    }

    public void setSourceBroker(BrokerConfig broker){
        this.sourceBroker = broker;
    }

    public boolean isSourceFolder(){
        return this.sourceFolder != null;
    }

    public String getSourceFolder() {
        return sourceFolder;
    }

    public void setSourceFolder(String source) {
        this.sourceFolder = source;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay < 0 ? 30 : delay; // delay cannot be negative
    }

    public static ClientConfig loadFromFile(Path config) throws IOException {

        // SNAKEYAML lib 2.0
        LoaderOptions options = new LoaderOptions();
        options.setAllowRecursiveKeys(true);
        options.setMaxAliasesForCollections(3);

        Constructor constructor = new Constructor(ClientConfig.class,options); //ClientConfig.class is root
        TypeDescription configDescription = new TypeDescription(ClientConfig.class);
        Representer representer = new Representer(new DumperOptions());
        representer.getPropertyUtils().setSkipMissingProperties(true);

        configDescription.addPropertyParameters("Broker", BrokerConfig.class);
        configDescription.addPropertyParameters("Authentication", Authentication.class);
        configDescription.addPropertyParameters("Topics", Topic.class);

        configDescription.substituteProperty("sourceBroker",BrokerConfig.class,"getSourceBroker","setSourceBroker");
        configDescription.setExcludes("sourceBroker"); //"sourceFolder",

        constructor.addTypeDescription(configDescription);
        representer.addTypeDescription(configDescription);
        Yaml yaml = new Yaml(constructor,representer);
        yaml.setBeanAccess(BeanAccess.FIELD);
        instance = yaml.load(Files.newInputStream(config));
        yaml.setBeanAccess(BeanAccess.DEFAULT);
        return instance;
    }

    public static ClientConfig getInstance() {
        return instance;
    }

    public static void main(String args[]) throws IOException {

        ClientConfig config = ClientConfig.loadFromFile(Path.of("config/config.yaml"));
        System.out.println(config.getBroker().getClientId());
        System.out.println(config.getSourceFolder());
        System.out.println(config.getDelay());
        //System.out.println(config.getSourceBroker().getClientId());
        System.out.println(config.getBroker().getAuthentication().hasCredentials());
    }
}

