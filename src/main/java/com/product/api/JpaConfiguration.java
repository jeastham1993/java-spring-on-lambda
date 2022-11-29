package com.product.api;
import com.google.gson.Gson;
import org.crac.Core;
import org.springframework.boot.jdbc.DataSourceBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class JpaConfiguration {
    private final Gson gson = new Gson();
    public JpaConfiguration()
    {
    }

    public DataSource dataSource() {
        final AwsSecret dbCredentials = getSecret();

        var dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUrl("jdbc:postgresql://" + System.getenv("DATABASE_ENDPOINT") + ":" + dbCredentials.getPort() + "/productapi");
        dataSource.setUsername(dbCredentials.getUsername());
        dataSource.setPassword(dbCredentials.getPassword());

        return dataSource;
    }

    private AwsSecret getSecret() {
        var secretsManagerClient = SecretsManagerClient.create();

        String secret;

        var getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(System.getenv("SECRET_NAME"))
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