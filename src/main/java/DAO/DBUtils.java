package DAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtils {
    private static String url;
    private static String userDB;
    private static String password;
    private static Properties properties;

    public static Connection getConnection() throws SQLException {
        if (properties == null) {
            try (InputStream fis = DBUtils.class.getResourceAsStream("/db.properties")) {
                properties = new Properties();
                properties.load(fis);
                url = (String) properties.get("url");
                userDB = (String) properties.get("userDB");
                password = (String) properties.get("password");
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return DriverManager.getConnection(url, userDB, password);
    }
}
