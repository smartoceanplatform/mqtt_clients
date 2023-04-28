# Publisher MQTT clients to provide data to a broker

Used for replaying data acquired by sensors or to bridge data from another broker.

## Configuration File
The application expects YAML configuration files to specify the MQTT connection properties where to publish the data and the source of the data to be published. By default the application uses the config.yaml under the [config/](config/) folder for publishing data from a folder, and config_dev_pub.yaml for publishing data from a broker.

### MQTT connection properties

In both configuration files you can use the top level ```broker``` node where you can specify the host, port, authentication credentials (can be empty), the topic where to publish, and the associated quality-of-service (QoS). Example of such configuration: 

```yaml
broker:
  host: "127.0.0.1"
  port: 1883
  clientId: "virtualProvider"
  authentication:
    username:
    password:
  topics:
      - topic: "my/test/topic1"
        qos: 2 # 0 - AT_MOST_ONCE  | 1 - AT_MOST_ONCE | 2 - EXACTLY_ONCE
``` 


### Data Sources

To configure a folder as a data source use the top level ```sourceFolder`` and ```delay`` nodes to define the path to the folder and the delay interval between published data from the folder.

To configure a broker as a data source use the top level ```sourceBroker``` node which uses the same key:values properties as the ```broker``` node described above. In this case, one could subcribe to multiple topics as sources of data using:

```yaml
sourceBroker:
  host: "127.0.0.1"
  port: 1883
  clientId: "bridgeProvider"
  authentication:
    username:
    password:
  topics:
      - topic: "my/test/topic1"
        qos: 2 
      - topic: "my/test/topic2"
        qos: 2
    # Wildcard Alternative:
    # - topic: "my/test/topic#"
    #    qos: 2
``` 

## Docker Build and Execution

```bash
docker build -t publisher .
docker run publisher:latest
```

To customize configuration parameters pass the absolut path of the YAML configuration file (*$PATH_TO_YAML*) as argument in the docker command:

```bash
docker run -v $PATH_TO_YAML:/etc/publisher/config.yaml publisher:latest
```

To mount a different data folder (*$PATH_TO_DIR*) override the following directory as argument in the docker command:

```bash
docker run -p 9091:9091 -v $PATH_TO_DIR:/data publisher:latest
```
