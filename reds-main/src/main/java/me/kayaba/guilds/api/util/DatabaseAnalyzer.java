package me.kayaba.guilds.api.util;

import java.sql.*;
import java.util.*;

public interface DatabaseAnalyzer {
    enum ModificationType {

        ADD,


        ADD_INSIDE,


        REMOVE,


        MOVE,


        RENAME,


        CHANGETYPE
    }


    void analyze(String table, String sql) throws SQLException;


    void update() throws SQLException;


    List<Missmatch> getMissmatches();

    interface Missmatch {

        int getIndex();


        ModificationType getModificationType();


        String getColumnName();


        String getPreviousColumn();


        String getColumnType();


        String getTableName();


        String getConstraints();
    }
}
