package com.github.kanesada2.OADBConnect;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import java.sql.DatabaseMetaData;

import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

public class OADBConnect extends JavaPlugin implements Listener{

	@Override
    public void onEnable() {
        String DB_URL = "jdbc:oracle:thin:@" + getConfig().getString("DBName_Medium") + "?TNS_ADMIN=" + getConfig().getString("TNS_Admin");
        String DB_USER = getConfig().getString("DB_User");
        String DB_PASSWORD = getConfig().getString("DB_Pass");
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        Properties info = new Properties();     
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);          
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");
        try{
          OracleDataSource ods = new OracleDataSource();
          ods.setURL(DB_URL);    
          ods.setConnectionProperties(info);
          OracleConnection connection = (OracleConnection) ods.getConnection();
          printUsers(connection);
        }catch(SQLException e){
          getLogger().info(String.valueOf(e.getErrorCode()));
        }
        getLogger().info("OADBConnect Enabled!");
    }

    @Override
    public void onDisable() {
    	
    }
    /*
  * Displays first_name and last_name from the employees table.
  */
  private static void printUsers(Connection connection) throws SQLException {
    // Statement and ResultSet are AutoCloseable and closed automatically. 
    try (Statement statement = connection.createStatement()) {      
      try (ResultSet resultSet = statement
          .executeQuery("select uuid, name from players")) {
        while (resultSet.next())
          Bukkit.getLogger().info(resultSet.getString(1) + " "
              + resultSet.getString(2) + " ");       
      }
    }   
  } 
}
