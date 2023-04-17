# MQTT clients to provide data to broker

Used for replaying data acquired by sensors.

## Docker Build and Execution

```bash
docker build -t publisher .
docker run publisher:latest
```

To customize configuration parameters pass the absolut path of the YAML configuration file (*$PATH_TO_YAML*) as argument in the docker command:

```bash
docker run -v $PATH_TO_YAML:/etc/config.yaml publisher:latest
```

To mount a different data folder (*$PATH_TO_DIR*) override the following directory as argument in the docker command:

```bash
docker run -p 9091:9091 -v $PATH_TO_DIR:/data publisher:latest
```
