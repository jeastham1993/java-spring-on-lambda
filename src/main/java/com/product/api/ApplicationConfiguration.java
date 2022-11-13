package com.product.api;
import com.google.gson.Gson;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

@Configuration
public class ApplicationConfiguration {
    private final Gson gson = new Gson();
    private ApplicationProperties properties;

    public ApplicationProperties getApplicationProperties()
    {
        if (properties != null)
        {
            return properties;
        }

        this.properties = getProps();

        return properties;
    }

    private ApplicationProperties getProps() {
        var parameterName = "/dev/spring-crud/properties";

        var ssmClient = SsmClient
                .builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.US_EAST_1)
                .build();

        String parameter;

        var getParameterRequest = GetParameterRequest.builder()
                .name(parameterName)
                .build();

        GetParameterResponse result = null;

        try {
            result = ssmClient.getParameter(getParameterRequest);
        }
        catch (Exception e) {
            throw e;
        }
        if (result.parameter().value() != null) {
            parameter = result.parameter().value();
            return gson.fromJson(parameter, ApplicationProperties.class);
        }

        return null;
    }
}
