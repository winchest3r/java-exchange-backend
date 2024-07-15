package io.github.winchest3r.backend.util;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import javax.sql.*;

import jakarta.servlet.*;

import com.zaxxer.hikari.*;

public class SqliteDataSource implements ServletContextListener {
    private static final String DB_NAME = "backend.db";

    private static HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String dbPath = sce.getServletContext().getResource(SqlQueries.BASE_PATH).getPath() + DB_NAME;

            // If database is not exists -> initialize it after creation
            boolean isInitialized = false;
            if (new File(dbPath).isFile()) {
                isInitialized = true;
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:sqlite:" + dbPath);
            config.setDriverClassName("org.sqlite.JDBC");
            dataSource = new HikariDataSource(config);

            if (!isInitialized) {
                initializeDatabase(sce);
            }

        } catch (Exception e) {
            System.out.println("Failed to initialize database: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database: " + e.getMessage());
        }
    }

    private static void initializeDatabase(ServletContextEvent sce) {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement();
                 BufferedReader createReader = new BufferedReader(
                    new InputStreamReader(
                        sce.getServletContext().getResourceAsStream(SqlQueries.CREATE_TABLES)
                    )
                 );
                 BufferedReader fillReader = new BufferedReader(
                    new InputStreamReader(
                        sce.getServletContext().getResourceAsStream(SqlQueries.FILL_TABLES)
                    )
                 )
                ) {
                
                String line;
                StringBuilder sql = new StringBuilder();

                // Initialize tables
                while ((line = createReader.readLine()) != null) {
                    sql.append(line).append("\n");
                    if (line.trim().endsWith(";")) {
                        statement.execute(sql.toString());
                        sql.setLength(0);
                    }
                }

                // Fill with initial data
                while ((line = fillReader.readLine()) != null) {
                    sql.append(line).append("\n");
                    if (line.trim().endsWith(";")) {
                        statement.execute(sql.toString());
                        sql.setLength(0);
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("Failed to initialize database: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dataSource != null) {
            dataSource.close();
        }
    }


    public static DataSource getDataSource() {
        return dataSource;
    }
}
