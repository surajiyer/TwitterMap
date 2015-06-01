/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * required library:
 * http://www.java2s.com/Code/Jar/m/Downloadmysqlconnectorjavajar.htm
 * 
 * @author s139662
 */
public class MySQL4j {

    /** MySQL database username */
    private final String username;
    
    /** MySQL database password */
    private final String password;
    
    /** MySQL database URL link */
    private final String url;
    
    /** Connection to MySQL database */
    private Connection conn;
    
    /** Date formats for console output. */
    private final SimpleDateFormat console_format;
    
    /**
     * Pass the MySQL database URL while using the default username ('root') and
     * password ('')
     * 
     * @param url link to the MySQL database of the forms, <br><b>
     *      * jdbc:mysql://hostname:port/database_name (commonly used port: 3306) <br>
     *      * jdbc:mysql://ip_address:port/database_name</b><br>
     */
    public MySQL4j(String url) {
        this("root", "", url);
    }
    
    /**
     * Pass the username, password and the MySQL database URL.
     * 
     * @param username the username of the database.
     * @param password the password of the database.
     * @param url link to the MySQL database of the forms, <br><b>
     *      * jdbc:mysql://hostname:port/database_name (commonly used port: 3306) <br>
     *      * jdbc:mysql://ip_address:port/database_name</b><br>
     */
    public MySQL4j(String username, String password, String url) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.conn = null;
        console_format = new SimpleDateFormat("hh:mm:ss");
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getURL() {
        return url;
    }
    
    /**
     * Establishes a connection to provided database URL with the given username
     * and password.
     * 
     * @throws java.lang.Exception if connection could not be established.
     */
    public void connect() throws Exception {
        // It returns the Class object associated with the class or interface with the given string name.
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        // DriverManager is the basic service for managing a set of JDBC drivers.
        // getConnection() attempts to establish a connection to the given database URL
        conn = DriverManager.getConnection(url, username, password);
        
        System.out.println(console_format.format(new Date(System.currentTimeMillis())) 
                + " INFO: Connection established");
    }
    
    /**
     * Executes the given SQL query on the database.
     * 
     * @param query the SQL query to execute.
     * @throws SQLException if query could not be executed.
     */
    public void executeSQLQuery(String query) throws SQLException {
        conn.createStatement().executeUpdate(query);
        System.out.println(console_format.format(new Date(System.currentTimeMillis())) 
                + " INFO: SQL query executed successfully");
    }

    /**
     * Close the connection to the database.
     * 
     * @throws SQLException if connection could not be properly terminated.
     */
    public void close() throws SQLException {
        conn.close();
        System.out.println(console_format.format(new Date(System.currentTimeMillis())) 
                + " INFO: Connection to database terminated successfully");
    }
    
    public static void main(String[] args) throws Exception {
        // test
        MySQL4j sql = new MySQL4j("s139662", "rvH6X6a7rN9bJtUD", 
                "jdbc:mysql://surajiyer96.synology.me:3306/twitter_filter_stream");
        sql.connect();
        sql.executeSQLQuery("INSERT tweets VALUES(602145169886969857,602036363181993986,"
                + "0,0,'RT @DrKumarVishwas: अच्छे दिन for corrupt officers 😜 #ModiMurdersDemocracy  http://t.co/EGwbwmQ1WH',"
                + "1432397570000,'und','hi',2371691228,NULL)");
        sql.close();
    }
}
