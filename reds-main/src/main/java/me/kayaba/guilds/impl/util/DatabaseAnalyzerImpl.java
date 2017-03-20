package me.kayaba.guilds.impl.util;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.util.*;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.*;

public class DatabaseAnalyzerImpl implements DatabaseAnalyzer {
    private final Connection connection;
    private final Map<String, String> sqlStructure = new HashMap<>();
    private final Map<Integer, String> sqlNames = new HashMap<>();
    private final Map<String, String> tableStructure = new HashMap<>();
    private final Map<Integer, String> tableNames = new HashMap<>();
    private final List<Missmatch> missmatches = new ArrayList<>();
    private final Map<String, String> sqlConstraints = new HashMap<>();


    public DatabaseAnalyzerImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void analyze(String table, String sql) throws SQLException {
        if (!existsTable(table)) {
            addTable(sql);
        }

        getSqlStructure(sql);
        getTableStructure(table);


        if (tableStructure.size() < sqlNames.size()) {
            int shift = 0;

            for (int index = 0; index + shift < sqlNames.size(); index++) {
                String n1 = tableNames.get(index);
                String n2 = sqlNames.get(index + shift);

                if (n2.equalsIgnoreCase(n1)) {
                    continue;
                }

                String after = tableNames.get(index - 1);
                shift++;
                Missmatch missmatch = new MissmatchImpl(ModificationType.ADD_INSIDE, table, index, n2, sqlStructure.get(n2), after, sqlConstraints.get(n2));
                missmatches.add(missmatch);
                LoggerUtils.info(" ADD_INSIDE: " + n2 + " (" + sqlStructure.get(n2) + ") after " + after, false);
            }
        }

        for (String columnName : tableNames.values()) {
            String typeTable = tableStructure.get(columnName);
            String typeSQL = sqlStructure.get(columnName);

            if (typeSQL.contains("(")) {
                StringBuilder buf = new StringBuilder(typeSQL);
                int start = typeSQL.indexOf("(");
                int end = typeSQL.indexOf(")") + 1;
                buf.replace(start, end, "");
                typeSQL = buf.toString();
            }

            if (typeSQL.equalsIgnoreCase(typeTable)) {
                continue;
            }

            Missmatch missmatch = new MissmatchImpl(ModificationType.CHANGETYPE, table, 0, columnName, sqlStructure.get(columnName), sqlConstraints.get(columnName));
            missmatches.add(missmatch);
            LoggerUtils.info(" CHANGETYPE: " + columnName + ": " + typeTable + " -> " + typeSQL, false);
        }
    }

    @Override
    public void update() throws SQLException {
        sort();

        for (Missmatch missmatch : missmatches) {
            switch (missmatch.getModificationType()) {
                case ADD_INSIDE:
                    addColumn(missmatch);
                    break;
                case CHANGETYPE:
                    changeType(missmatch);
                    break;
            }
        }
    }

    @Override
    public List<Missmatch> getMissmatches() {
        return missmatches;
    }


    private void addColumn(Missmatch missmatch) throws SQLException {
        String sql = "ALTER TABLE `" + missmatch.getTableName() + "` ADD COLUMN `" + missmatch.getColumnName() + "` " + missmatch.getColumnType() + " " + missmatch.getConstraints() + " AFTER `" + missmatch.getPreviousColumn() + "`;";
        Statement statement = connection.createStatement();
        statement.execute(sql);
        LoggerUtils.info("Added new column " + missmatch.getColumnName() + " after " + missmatch.getPreviousColumn() + " to table " + missmatch.getTableName());
    }


    private void changeType(Missmatch missmatch) throws SQLException {
        String sql = "ALTER TABLE `" + missmatch.getTableName() + "` MODIFY `" + missmatch.getColumnName() + "` " + missmatch.getColumnType() + ";";
        Statement statement = connection.createStatement();
        statement.execute(sql);
        LoggerUtils.info("Changed column " + missmatch.getColumnName() + " type to " + missmatch.getColumnType());
    }


    private void addTable(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sql);
        LoggerUtils.info("Added new table");
    }


    private void sort() {
        Collections.sort(missmatches, new Comparator<Missmatch>() {
            public int compare(Missmatch o1, Missmatch o2) {
                return o1.getIndex() - o2.getIndex();
            }
        });
    }


    private void getSqlStructure(String sql) {
        sqlNames.clear();
        sqlStructure.clear();
        int i = 0;

        for (String c : StringUtils.split(sql, ",\r\n")) {
            if (c.startsWith("  `")) {
                String[] split = StringUtils.split(c, ' ');
                String name = StringUtils.replace(split[0], "`", "");
                String type = split[1];
                String constraints = StringUtils.replace(c, "  " + split[0] + " " + split[1], "");

                if (split[2].equalsIgnoreCase("unsigned")) {
                    type += " " + split[2];
                }

                sqlStructure.put(name, type);
                sqlNames.put(i, name);
                sqlConstraints.put(name, constraints);
                i++;
            }
        }
    }


    private void getTableStructure(String table) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet columns = databaseMetaData.getColumns(null, null, table, null);
        tableNames.clear();
        tableStructure.clear();
        int i = 0;

        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String columnType = columns.getString("TYPE_NAME");

            tableNames.put(i, columnName);
            tableStructure.put(columnName, columnType);
            i++;
        }

        columns.close();
    }


    private boolean existsTable(String table) throws SQLException {
        DatabaseMetaData md = connection.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);

        while (rs.next()) {
            if (rs.getString(3).equalsIgnoreCase(table)) {
                return true;
            }
        }

        return false;
    }

    public class MissmatchImpl implements DatabaseAnalyzer.Missmatch {
        private final int index;
        private final String table;
        private final String columnName;
        private final String columnType;
        private final String previousColumn;
        private final String constraints;
        private final ModificationType modificationType;


        public MissmatchImpl(ModificationType modificationType, String table, int index, String columnName, String columnType, String previousColumn, String constraints) {
            this.modificationType = modificationType;
            this.table = table;
            this.index = index;
            this.columnName = columnName;
            this.columnType = columnType;
            this.previousColumn = previousColumn;
            this.constraints = constraints;
        }


        public MissmatchImpl(ModificationType modificationType, String table, int index, String columnName, String columnType, String constraints) {
            this(modificationType, table, index, columnName, columnType, "", constraints);
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public ModificationType getModificationType() {
            return modificationType;
        }

        @Override
        public String getColumnName() {
            return columnName;
        }

        @Override
        public String getColumnType() {
            return columnType;
        }

        @Override
        public String getPreviousColumn() {
            return previousColumn;
        }

        @Override
        public String getTableName() {
            return table;
        }

        @Override
        public String getConstraints() {
            return constraints;
        }
    }
}
