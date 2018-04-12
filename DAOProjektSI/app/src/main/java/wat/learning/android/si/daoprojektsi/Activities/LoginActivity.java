package wat.learning.android.si.daoprojektsi.Activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

import wat.learning.android.si.daoprojektsi.ActionCode;
import wat.learning.android.si.daoprojektsi.Database.DatabaseConnection;
import wat.learning.android.si.daoprojektsi.Database.DatabaseService;
import wat.learning.android.si.daoprojektsi.Database.MyResultReceiver;
import wat.learning.android.si.daoprojektsi.Fragments.Login.LoginFragment;
import wat.learning.android.si.daoprojektsi.Fragments.Login.ProgressFragment;
import wat.learning.android.si.daoprojektsi.Fragments.Login.ResetPasswordFragment;
import wat.learning.android.si.daoprojektsi.R;


public class LoginActivity extends Activity implements MyResultReceiver.Receiver, Serializable {

    private MyResultReceiver mReceiver;
    private int lokatorId;
    private Connection connection = null;
    private DatabaseConnection dbConn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        showProgress();
        dbConnection();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch(resultCode){
            case 0: //LOGIN_SUCCESS
                lokatorId = resultData.getInt("lokatorId");
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.putExtra("id", lokatorId);
                mainIntent.putExtra("Connection", new Gson().toJson(dbConn));
                startActivity(mainIntent);
                break;
            case 1: //LOGIN_FAILURE
                showLogin();
                String error = resultData.getString("Error");
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                break;
            case 2: //SEED_CONFIRMED
                lokatorId = resultData.getInt("lokatorId");
                Toast.makeText(this, "Seed check succesaful.", Toast.LENGTH_SHORT).show();
                showResetPassword();
                break;
            case 3: //PASSWORD_CHANGED
                String msg = resultData.getString("msg");
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                showLogin();
                break;
            case 4: //PASSWORD_CHANGE_FAILED
                showResetPassword();
                Toast.makeText(this, resultData.getString("Error"), Toast.LENGTH_SHORT).show();
                break;
            case 5: //NEW_CONNECTION
                String jsonConnection;
                jsonConnection = resultData.getString("Connection");
                dbConn = new Gson().fromJson(jsonConnection, DatabaseConnection.class);
                connection = dbConn.getConnection();
                showLogin();
                break;

        }
    }

    public void attemptLogin(String email, String password) {
        showProgress();
            try {
                String hashPassword = hash256(password);
                Intent intent = new Intent(this, DatabaseService.class);
                intent.putExtra("receiverTag", mReceiver);
                intent.putExtra("code", ActionCode.LOGIN);
                intent.putExtra("email", email);
                intent.putExtra("hashPassword", hashPassword);
                intent.putExtra("seed", password);
                intent.putExtra("Connection", new Gson().toJson(dbConn));
                startService(intent);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();

            }
        }

    public void dbConnection() {
        Intent connIntent = new Intent(this,DatabaseService.class);
        connIntent.putExtra("receiverTag", mReceiver);
        connIntent.putExtra("code", ActionCode.CREATE_CONNECTION);
        startService(connIntent);
    }

    public void resetPassword(String pass) {
        showProgress();
        try {
            String hashNewPassword = hash256(pass);
            Intent intent = new Intent(this, DatabaseService.class);
            intent.putExtra("receiverTag", mReceiver);
            intent.putExtra("code", ActionCode.PASSWORD_RESET);
            intent.putExtra("id", lokatorId);
            intent.putExtra("hashNewPassword", hashNewPassword);
            intent.putExtra("Connection", new Gson().toJson(dbConn));
            startService(intent);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void showProgress(){
        hideKeyboard(this);
        ProgressFragment progressFragment = new ProgressFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, progressFragment);
        ft.commit();
    }

    public void showLogin(){
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, loginFragment);
        ft.commit();
    }

    public void showResetPassword(){
        ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, resetPasswordFragment);
        ft.commit();
    }

    public static String hash256(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data.getBytes());
        return bytesToHex(md.digest());
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    public static void hideKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

