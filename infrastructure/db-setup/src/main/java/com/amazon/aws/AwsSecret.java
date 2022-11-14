package com.amazon.aws;

public class AwsSecret {
    private String username;
    private String password;
    private String host;
    private String engine;
    private String port;
    private String dbInstanceIdentifier;
    private String dbname;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDbname() {
        return dbname;
    }

    public String getHost() {
        return host;
    }

    public String getEngine() {
        return engine;
    }

    public String getPort() {
        return port;
    }

    public String getDbInstanceIdentifier() {
        return dbInstanceIdentifier;
    }
}
