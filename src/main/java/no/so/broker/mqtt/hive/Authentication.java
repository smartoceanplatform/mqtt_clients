package no.so.broker.mqtt.hive;

public class Authentication {
    private String username;
    private String password;

    public Authentication(){

    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean hasCredentials(){
        return (this.username != null && this.password != null);
    }
}
