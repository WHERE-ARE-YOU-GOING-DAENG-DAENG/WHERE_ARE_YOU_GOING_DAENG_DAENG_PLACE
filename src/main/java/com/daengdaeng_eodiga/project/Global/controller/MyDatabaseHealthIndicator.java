package com.daengdaeng_eodiga.project.Global.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component("myDbHealthIndicator")
public class MyDatabaseHealthIndicator implements HealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(MyDatabaseHealthIndicator.class);

    @Autowired(required = false)
    private DataSource dataSource;

    private static final String VALIDATION_QUERY = "SELECT 1";

    @Override
    public Health health() {
        // DataSource 빈이 없으면 체크 불가 상태 반환 (선택 사항)
        if (dataSource == null) {
            return Health.unknown().withDetail("reason", "DataSource bean is not available").build();
        }

        if (isDbConnected()) {
            return Health.up().withDetail("database", "available").build();
        } else {
            return Health.down().withDetail("database", "unavailable").build();
        }
    }

    private boolean isDbConnected() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.executeQuery(VALIDATION_QUERY);

            return true;
        } catch (Exception e) {
            log.warn("Readiness check: Database connection check failed - {}", e.getMessage());
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    log.warn("Error closing statement", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    log.warn("Error closing connection", e);
                }
            }
        }
    }
}