package me.kayaba.guilds.api.storage;

import java.sql.*;


public interface Database {

    void openConnection() throws SQLException, ClassNotFoundException;


    boolean checkConnection() throws SQLException;


    Connection getConnection();


    boolean closeConnection() throws SQLException;
}
