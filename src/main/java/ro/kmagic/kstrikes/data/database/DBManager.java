package ro.kmagic.kstrikes.data.database;

import com.mysql.cj.jdbc.Blob;
import ro.kmagic.kstrikes.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class DBManager {

    private final DBConnection dbConnection;

    public DBManager() {
        this.dbConnection = null;
    }

    public DBManager(DBConnection connection) {
        this.dbConnection = connection;
    }

    public DBConnection getDbConnection() {
        return dbConnection;
    }

    public PreparedStatement getPreparedStatement(String query) {
        try {
            assert this.dbConnection != null;
            return this.dbConnection
                    .getConnection().prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.error("Error when preparing a database statement! Send the following to the dev.");
            return null;
        }
    }

    public void executeStatement(String query) {
        try {
            assert this.dbConnection != null;
            Statement statement = this.dbConnection
                    .getConnection().createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.error("Error when executing a database statement! Send the following to the dev.");
        }
    }


    public void execute(PreparedStatement preparedStatement) {
        CompletableFuture.runAsync(() -> {
            try {
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                Utils.error("Error when executing a prepared statement! Report this to the dev.");
            }
        });
    }


    public void executeUpdate(PreparedStatement preparedStatement) {
        CompletableFuture.runAsync(() -> {
            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                Utils.error("Error when trying to execute an update! Report this to the dev.");
            }
        });
    }


    public ResultSet executeQuery(PreparedStatement preparedStatement) {
        CompletableFuture<ResultSet> res = CompletableFuture.supplyAsync(() -> {
            try {
                return preparedStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
                Utils.error("Error when trying to execute a query to the database! Report this to the dev.");
                return null;
            }
        });
        try {
            return res.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Utils.error("Error when trying to get the value from an executed query to the database! Report this to the dev.");
            return null;
        }
    }


    public <T> boolean exists(String tableName, String field, T obj) {
        PreparedStatement preparedStatement = this.getPreparedStatement("SELECT * FROM `" + tableName + "` WHERE " + field + " = ?");
        try {
            preparedStatement.setObject(1, obj);
            ResultSet resultSet = this.executeQuery(preparedStatement);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.error("Error when getting values within tables! Report this to the dev.");
            return false;
        }
    }


    public boolean existString(String tableName, String field, String lookup) {
        return exists(tableName, field, lookup);
    }


    public boolean existsInt(String tableName, String field, int lookup) {
        return exists(tableName, field, lookup);
    }


    public boolean existsUniqueIdentifier(String tableName, String field, UUID lookup) {
        return exists(tableName, field, lookup);
    }


    public boolean existsLong(String tableName, String field, long lookup) {
        return exists(tableName, field, lookup);
    }


    public boolean existsBlob(String tableName, String field, Blob lookup) {
        return exists(tableName, field, lookup);
    }


    public ResultSet get(String tableName, String keyField, Object key) {
        String query = "SELECT * FROM `" + tableName + "` WHERE " + keyField + "=?";
        PreparedStatement preparedStatement = this.getPreparedStatement(query);
        try {
            preparedStatement.setObject(1, key);
            ResultSet resultSet = this.executeQuery(preparedStatement);
            if (!resultSet.next()) {
                return null;
            }
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.error("Error when getting values from plugin tables! Report this to the dev.");
            return null;
        }
    }


    public <T> void set(String tableName, String setField, String keyField, Object set, T key) {
        String query = "UPDATE " + tableName + " SET " + setField + " = ? WHERE " + keyField + " = ?";
        PreparedStatement preparedStatement = this.getPreparedStatement(query);
        try {
            preparedStatement.setObject(1, set);
            preparedStatement.setObject(2, key);
            this.executeUpdate(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.error("Error when setting values into plugin tables! Report this to the dev.");
        }
    }


    public <T> void insert(String tableName, String[] fields, T[] args) {
        if (fields.length != args.length) {
            throw new Error("Arguments in string array `fields` have to match arguments in T array `args`!");
        }
        AtomicReference<String> query = new AtomicReference<>("INSERT INTO `" + tableName + "`");
        query.set(query.get() + " VALUES (");
        for (int i = 0; i < args.length; i++) {
            query.set(query.get() + "?, ");
        }
        query.set(this.fixLastIndex(query.get()));
        PreparedStatement preparedStatement = this.getPreparedStatement(query.get());
        for (int i = 1; i <= args.length; i++) {
            try {
                preparedStatement.setObject(i, args[i - 1]);
            } catch (SQLException e) {
                e.printStackTrace();
                Utils.error("Error when inserting into plugin tables! Report this to the dev.");
            }
        }
        this.execute(preparedStatement);
    }


    public void createTable(String tableName, String[] args) {
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS `" + tableName + "` (");
        for (String arg : args) {
            query.append(arg).append(", ");
        }
        query = new StringBuilder(query.substring(0, query.length() - 2) + ")");
        PreparedStatement preparedStatement = this.getPreparedStatement(query.toString());
        try {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.error("Error when creating plugin tables! Report this to the dev.");
        }
    }


    private String fixLastIndex(String string) {
        String ret = "";
        ret = string.substring(0, string.length() - 2) + ")";
        return ret;
    }


    public void setString(String tableName, String setField, String keyField, String set, Object key) {
        set(tableName, setField, keyField, set, key);
    }


    public void setInt(String tableName, String setField, String keyField, int set, Object key) {
        set(tableName, setField, keyField, set, key);
    }


    public void setBoolean(String tableName, String setField, String keyField, boolean set, Object key) {
        set(tableName, setField, keyField, set, key);
    }


    public void setLong(String tableName, String setField, String keyField, long set, Object key) {
        set(tableName, setField, keyField, set, key);
    }


    public String getString(String tableName, String getField, String keyField, Object key) throws SQLException {
        return get(tableName, keyField, key).getString(getField);
    }


    public int getInt(String tableName, String getField, String keyField, Object key) throws SQLException {
        return get(tableName, keyField, key).getInt(getField);
    }


    public boolean getBoolean(String tableName, String getField, String keyField, Object key) throws SQLException {
        return get(tableName, keyField, key).getBoolean(getField);
    }


    public Long getLong(String tableName, String getField, String keyField, Object key) throws SQLException {
        return get(tableName, keyField, key).getLong(getField);
    }

}
