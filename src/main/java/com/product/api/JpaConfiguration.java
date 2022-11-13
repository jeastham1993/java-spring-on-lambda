package com.product.api;
import com.google.gson.Gson;
import org.springframework.boot.jdbc.DataSourceBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class JpaConfiguration {
    private final Gson gson = new Gson();

    public DataSource dataSource() {
        final AwsSecret dbCredentials = getSecret();

        return DataSourceBuilder
                .create()
                .url("jdbc:postgresql://" + dbCredentials.getHost() + ":" + dbCredentials.getPort() + "/productdb")
                .username(dbCredentials.getUsername())
                .password(dbCredentials.getPassword())
                .build();
    }

    private AwsSecret getSecret() {
        var secretName = "product-db-cred";

        var secretsManagerClient = SecretsManagerClient
                .builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.US_EAST_1)
                .build();

        String secret;

        var getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse result = null;

        try {
            result = secretsManagerClient.getSecretValue(getSecretValueRequest);
        }
        catch (Exception e) {
            throw e;
        }
        if (result.secretString() != null) {
            secret = result.secretString();
            return gson.fromJson(secret, AwsSecret.class);
        }

        return null;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.POSTGRESQL);
        jpaVendorAdapter.setGenerateDdl(true);

        return jpaVendorAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lemfb = new LocalContainerEntityManagerFactoryBean();
        lemfb.setDataSource(dataSource());
        lemfb.setJpaVendorAdapter(jpaVendorAdapter());
        lemfb.setPackagesToScan("com.product.api");
        return lemfb;
    }


}