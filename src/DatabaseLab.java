import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseLab {
    // Database connection parameters - modify these according to your setup
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/act5_university";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "kleenahida1313";

    private Connection connection;

    public DatabaseLab() {
        try {
            // Load PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            // Establish connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Feature 1: Insert into relationname value1 value2 value3 ..
     * Uses prepared statements to safely insert data
     */
    public void insertIntoTable(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: insert into <table_name> <value1> [value2] [value3] ...");
            return;
        }

        String tableName = args[2];
        List<String> values = new ArrayList<>();

        // Collect all values from command line
        for (int i = 3; i < args.length; i++) {
            values.add(args[i]);
        }

        try {
            // Build prepared statement with appropriate number of placeholders
            StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
            for (int i = 0; i < values.size(); i++) {
                sql.append("?");
                if (i < values.size() - 1) {
                    sql.append(", ");
                }
            }
            sql.append(")");

            PreparedStatement pstmt = connection.prepareStatement(sql.toString());

            // Set all parameters
            for (int i = 0; i < values.size(); i++) {
                pstmt.setString(i + 1, values.get(i));
            }

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Insert successful! Rows affected: " + rowsAffected);
            System.out.println("Executed query: " + sql.toString());
            System.out.println("With values: " + values);

            pstmt.close();

        } catch (SQLException e) {
            System.err.println("Insert failed!");
            e.printStackTrace();
        }
    }

    /**
     * Feature 2: select from relationname
     * Prints all tuples from the specified relation
     */
    public void selectFromTable(String tableName) {
        try {
            String sql = "SELECT * FROM " + tableName;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Get metadata to determine column count and names
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Print column headers
            System.out.println("\n=== Results from " + tableName + " ===");
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t\t");
            }
            System.out.println();

            // Print separator
            for (int i = 1; i <= columnCount; i++) {
                System.out.print("--------\t");
            }
            System.out.println();

            // Print all rows
            int rowCount = 0;
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if (value == null) value = "NULL";
                    System.out.print(value + "\t\t");
                }
                System.out.println();
                rowCount++;
            }

            System.out.println("\nTotal rows: " + rowCount);

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println("Select failed!");
            e.printStackTrace();
        }
    }

    /**
     * Feature 3: select from relationname where "condition"
     * Executes a query with the specified condition
     */
    public void selectWithCondition(String tableName, String condition) {
        try {
            String sql = "SELECT * FROM " + tableName + " WHERE " + condition;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Get metadata
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Print column headers
            System.out.println("\n=== Results from " + tableName + " WHERE " + condition + " ===");
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t\t");
            }
            System.out.println();

            // Print separator
            for (int i = 1; i <= columnCount; i++) {
                System.out.print("--------\t");
            }
            System.out.println();

            // Print matching rows
            int rowCount = 0;
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if (value == null) value = "NULL";
                    System.out.print(value + "\t\t");
                }
                System.out.println();
                rowCount++;
            }

            System.out.println("\nRows matching condition: " + rowCount);
            System.out.println("Executed SQL: " + sql);

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println("Select with condition failed!");
            e.printStackTrace();
        }
    }

    /**
     * Feature 4: select from relationname1 relationname2
     * Displays result of natural join of the two relations
     */
    public void naturalJoin(String table1, String table2) {
        try {
            String sql = "SELECT * FROM " + table1 + " NATURAL JOIN " + table2;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Get metadata
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Print column headers
            System.out.println("\n=== Natural Join of " + table1 + " and " + table2 + " ===");
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t\t");
            }
            System.out.println();

            // Print separator
            for (int i = 1; i <= columnCount; i++) {
                System.out.print("--------\t");
            }
            System.out.println();

            // Print joined results
            int rowCount = 0;
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if (value == null) value = "NULL";
                    System.out.print(value + "\t\t");
                }
                System.out.println();
                rowCount++;
            }

            System.out.println("\nTotal joined rows: " + rowCount);
            System.out.println("Executed SQL: " + sql);

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println("Natural join failed!");
            e.printStackTrace();
        }
    }

    public void processCommand(String[] args) {
        if (args.length < 1) {
            printUsage();
            return;
        }

        String command = args[0].toLowerCase();

        switch (command) {
            case "insert":
                if (args.length >= 2 && args[1].equalsIgnoreCase("into")) {
                    insertIntoTable(args);
                } else {
                    System.out.println("Usage: insert into <table_name> <value1> [value2] ...");
                }
                break;

            case "select":
                if (args.length >= 3 && args[1].equalsIgnoreCase("from")) {
                    String tableName = args[2];

                    if (args.length >= 5 && args[3].equalsIgnoreCase("where")) {
                        // Feature 3: SELECT with WHERE condition
                        String condition = args[4];
                        selectWithCondition(tableName, condition);
                    } else if (args.length == 4) {
                        // Feature 4: Natural join (two table names)
                        String table2 = args[3];
                        naturalJoin(tableName, table2);
                    } else if (args.length == 3) {
                        // Feature 2: Simple SELECT
                        selectFromTable(tableName);
                    } else {
                        System.out.println("Invalid select syntax");
                        printUsage();
                    }
                } else {
                    System.out.println("Usage: select from <table_name> [where \"condition\"] or select from <table1> <table2>");
                }
                break;

            default:
                System.out.println("Unknown command: " + command);
                printUsage();
        }
    }

    private void printUsage() {
        System.out.println("\nUsage:");
        System.out.println("1. insert into <table_name> <value1> [value2] [value3] ...");
        System.out.println("2. select from <table_name>");
        System.out.println("3. select from <table_name> where \"condition\"");
        System.out.println("4. select from <table1> <table2>");
        System.out.println("\nSupported tables: instructor, student, takes");
    }

    public static void main(String[] args) {
        DatabaseLab lab = new DatabaseLab();

        if (lab.connection == null) {
            System.err.println("Failed to connect to database. Exiting.");
            return;
        }

        try {
            lab.processCommand(args);
        } finally {
            lab.closeConnection();
        }
    }
}
