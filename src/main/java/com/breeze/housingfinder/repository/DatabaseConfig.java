package com.breeze.housingfinder.repository;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
@PropertySource("classpath:config.properties")
public class DatabaseConfig {

    @Value("${dbUser}")
    private String dbUser;
    @Value("${dbPassword}")
    private String dbPassword;
    @Value("${dbJdbcUrl}")
    private String dbJdbcUrl;

    private DataSource dataSource;
    private ResourceLoader resourceLoader;

    @Autowired
    public DatabaseConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public DataSource dataSource() {
        try {
            Resource resource = resourceLoader.getResource("classpath:sql/initialize.sql");
            String initializeSql = IOUtils.toString(resource.getInputStream());

            // configure
            ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setDriverClass("org.h2.Driver");
            cpds.setJdbcUrl(dbJdbcUrl);
            cpds.setUser(dbUser);
            cpds.setPassword(dbPassword);

            // assign
            dataSource = cpds;
            Connection connection = dataSource.getConnection();
            System.out.println("Connection established to: " +
                    "database: " + connection.getMetaData().getDatabaseProductName() +
                    " version: " + connection.getMetaData().getDatabaseProductVersion());

            // initialization
            connection.prepareStatement(initializeSql).execute();

        } catch (Exception e) {
            e.printStackTrace();
            dataSource = null;
        }
        return dataSource;
    }
}
