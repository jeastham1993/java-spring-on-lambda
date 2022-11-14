package com.amazon.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.commons.io.IOUtils;
import software.amazon.lambda.powertools.parameters.ParamManager;
import software.amazon.lambda.powertools.parameters.SecretsProvider;
import software.amazon.lambda.powertools.parameters.transform.Transformer;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBSetupHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final String DB_CONNECTION = System.getenv("DB_CONNECTION_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String SECRET_NAME = System.getenv("SECRET_NAME");

    private static SecretsProvider secretsProvider;

    public DBSetupHandler(){
        secretsProvider = ParamManager.getSecretsProvider();
    }

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        AwsSecret secret = secretsProvider
                .withTransformation(Transformer.json)
                .get(SECRET_NAME, AwsSecret.class);

        try(var connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, secret.getPassword())) {
            try(var statement = connection.createStatement()) {
                try(var sqlFile = getClass().getClassLoader().getResourceAsStream("setup.sql")) {
                    statement.executeUpdate(IOUtils.toString(sqlFile));
                    return new APIGatewayProxyResponseEvent()
                            .withStatusCode(200)
                            .withBody("DB Setup successful");
                }
            }
        } catch (SQLException | IOException sqlException) {
            context.getLogger().log("Error connection to the database:" + sqlException.getMessage());
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("Error initializing the database");
        }
    }
}