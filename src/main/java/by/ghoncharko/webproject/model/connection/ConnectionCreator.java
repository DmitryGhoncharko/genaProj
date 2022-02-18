package by.ghoncharko.webproject.model.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ConnectionCreator class
 *
 * @author Dmitry Ghoncharko
 */

class ConnectionCreator {

    private static final Logger LOG = LogManager.getLogger(ConnectionCreator.class);
    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_FILE_NAME = "database.properties";
    private static final String PROPERTY_URL = "db.url";
    private static final String PROPERTY_USER = "db.user";
    private static final String PROPERTY_PASSWORD = "db.password";
    private static final String PROPERTY_DRIVER = "db.driver";
    private static final String DATABASE_URL;
    private static final String DATABASE_USER;
    private static final String DATABASE_PASSWORD;
    private static final String DATABASE_DRIVER;

    static {
        try (InputStream inputStream = ConnectionCreator.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE_NAME)) {
            PROPERTIES.load(inputStream);

            DATABASE_URL = PROPERTIES.getProperty(PROPERTY_URL);
            DATABASE_USER = PROPERTIES.getProperty(PROPERTY_USER);
            DATABASE_PASSWORD = PROPERTIES.getProperty(PROPERTY_PASSWORD);
            DATABASE_DRIVER = PROPERTIES.getProperty(PROPERTY_DRIVER);
            Class.forName(DATABASE_DRIVER);
        } catch (FileNotFoundException e) {
            LOG.fatal("FileNotFoundException", e);
            throw new RuntimeException("FileNotFoundException", e);
        } catch (IOException e) {
            LOG.fatal("IOException", e);
            throw new RuntimeException("IOException", e);
        } catch (ClassNotFoundException e) {
            LOG.fatal("ClassNotFoundException", e);
            throw new RuntimeException("ClassNotFoundException", e);
        }
    }

    private ConnectionCreator() {
    }

    /**
     * @return new connection to database
     * @throws SQLException when DATABASE_URL or DATABASE_USER or DATABASE_PASSWORD is incorrect
     */
    static Connection getConnecion() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }
}
