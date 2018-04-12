package wat.learning.android.si.daoprojektsi.Database;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import wat.learning.android.si.daoprojektsi.ActionCode;

/**
 * Created by Piotr on 21.03.2018.
 */

public class DatabaseService extends IntentService implements Database  {

    private static final int LOGIN_SUCCESS = 0;
    private static final int LOGIN_FAILURE = 1;
    private static final int SEED_CONFIRMED = 2;
    private static final int PASSWORD_CHANGED = 3;
    private static final int PASSWORD_CHANGE_FAILED = 4;
    private static final int NEW_CONNECTION = 5;
    private static final int MEDIA_LIST = 6;

    private ResultReceiver resultReceiver;
    private Connection connection = null;
    private Statement stmt;
    private Bundle bundle;
    private ResultSet resultSet;

    public DatabaseService() {
        super("DatabaseService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        resultReceiver = intent.getParcelableExtra("receiverTag");
        ActionCode action_code = (ActionCode) intent.getSerializableExtra("code");

        if(action_code == ActionCode.CREATE_CONNECTION){
            createConnection();
        } else {
            String jsonConnection;
            jsonConnection = intent.getStringExtra("Connection");
            DatabaseConnection dbConn = new Gson().fromJson(jsonConnection, DatabaseConnection.class);
            connection = dbConn.getConnection();
        }

        int lokatorId;
        switch(action_code){
            case LOGIN:
                String email = intent.getStringExtra("email");
                String seed = intent.getStringExtra("seed");
                String hashPassword = intent.getStringExtra("hashPassword");
                UserLogin(email, seed, hashPassword);
                break;
            case PASSWORD_RESET:
                lokatorId = intent.getIntExtra("id", 0);
                String hashNewPassword = intent.getStringExtra("hashNewPassword");
                UserPasswordReset(lokatorId, hashNewPassword);
                break;
            case LOGOUT:
                lokatorId = intent.getIntExtra("id", 0);
                //UserLogout(lokatorId);
                break;
            case GET_MEDIA:
                GetMediaFromDb();
                break;
            default:
                break;
        }
    }

    private void createConnection() {
        String connectionURL = "jdbc:jtds:sqlserver://projektsi.database.windows.net:1433/projektSI;"
                + "user=pchorekLogin@projektsi;"
                + "password=1toJestSekretneHasloPchora2;"
                + "encrypt=true;"
                + "trustServerCertificate=false;"
                + "hostNameInCertificate=*.database.windows.net;"
                + "loginTimeout=30;";

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionURL);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        bundle = new Bundle();
        bundle.putString("Connection", new Gson().toJson(new DatabaseConnection(connection)));
        resultReceiver.send(NEW_CONNECTION, bundle);
    }

    @Override
    public void UserLogin(String email, String seed, String hashPassword) {
        String userLoginQuery = "SELECT IdLokatora FROM Lokator WHERE Email=N'" + email + "' AND Haslo='" + hashPassword +"' AND Aktywny=1;";
        resultSet = getResultSet(userLoginQuery);
        try {
            if(resultSet.next()){
                int lokatorId = resultSet.getInt(1);
                if(lokatorId != 0){
                    //getResultSet("UPDATE Lokator SET Aktywny=True WHERE IdLokatora='"+lokatorId+"';");
                    bundle = new Bundle();
                    bundle.putInt("lokatorId", lokatorId);
                    resultReceiver.send(LOGIN_SUCCESS, bundle);
                    //clear();
                }
            } else {
                if(seed.length() == 16)
                UserSeedCheck(email, seed);
                else {
                    bundle = new Bundle();
                    bundle.putString("Error", "Wrong email or password. Try again!");
                    resultReceiver.send(LOGIN_FAILURE, bundle);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void UserSeedCheck(String email, String seed) {
        String userSeedQuery = "SELECT IdLokatora FROM Lokator WHERE Email=N'" + email + "' AND SeedResetowania='" + seed +"';";
        resultSet = getResultSet(userSeedQuery);
        try {
            if(resultSet.next()) {
                int lokatorId = resultSet.getInt(1);
                if (lokatorId != 0) {
                    bundle = new Bundle();
                    bundle.putInt("lokatorId", lokatorId);
                    resultReceiver.send(SEED_CONFIRMED, bundle);
                }
            } else {
                bundle = new Bundle();
                bundle.putString("Error", "Wrong email or password. Try again!");
                resultReceiver.send(LOGIN_FAILURE, bundle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
       //clear();
    }

    @Override
    public void UserPasswordReset(int lokatorId, String newPassword) {
        String userPasswordResetQuery = "UPDATE Lokator SET haslo='" + newPassword + "', SeedResetowania=NULL WHERE IdLokatora='"+ lokatorId +"';";
        resultSet = getResultSet(userPasswordResetQuery);
        String isUpdatedQuery = "Select haslo From Lokator WHERE IdLokatora='"+lokatorId+"';";
        resultSet = getResultSet(isUpdatedQuery);
        try {
            if (resultSet.next()){
                String changedPass = resultSet.getString(1);
                if(changedPass.equals(newPassword))
                bundle = new Bundle();
                bundle.putString("msg", "Password changed!");
                resultReceiver.send(PASSWORD_CHANGED, bundle);
            } else {
                bundle = new Bundle();
                bundle.putString("Error", "Password change operation failed!");
                resultReceiver.send(PASSWORD_CHANGE_FAILED,bundle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void GetMediaFromDb() {
        ArrayList<String> list = new ArrayList<>();
        String query = "SELECT * From Media;";
        resultSet = getResultSet(query);
        try {
            while (resultSet.next()){
                String media = resultSet.getString("NazwaMedia");
                list.add(media);
            }
                bundle = new Bundle();
                bundle.putStringArrayList("MediaList", list);
                resultReceiver.send(MEDIA_LIST,bundle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void UserLogout(int lokatorId) {
//        String userLogoutQuery = "UPDATE Lokator SET Aktywny=0 WHERE IdLokatora='"+lokatorId+"';";
//        getResultSet(userLogoutQuery);
//        bundle = new Bundle();
//        bundle.putString("msg","You are logged out!");
//        resultReceiver.send(5, bundle);
//    }

    private ResultSet getResultSet(String query){
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            //stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    private void clear(){
        try {
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
