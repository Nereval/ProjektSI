package wat.learning.android.si.daoprojektsi.Database;


import java.sql.Connection;

public class DatabaseConnection {

    private static Connection connection;

    public DatabaseConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection(){
        return connection;
    }

//    public void setConnection(Connection connection){
//        this.connection = connection;
//    }
}
