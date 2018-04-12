package wat.learning.android.si.daoprojektsi.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.sql.Connection;
import java.util.ArrayList;

import wat.learning.android.si.daoprojektsi.ActionCode;
import wat.learning.android.si.daoprojektsi.Database.DatabaseConnection;
import wat.learning.android.si.daoprojektsi.Database.DatabaseService;
import wat.learning.android.si.daoprojektsi.Database.MyResultReceiver;
import wat.learning.android.si.daoprojektsi.Fragments.Login.LoginFragment;
import wat.learning.android.si.daoprojektsi.Fragments.Main.ButtonsFragment;
import wat.learning.android.si.daoprojektsi.Fragments.Main.MiesieczneOplatyFragment;
import wat.learning.android.si.daoprojektsi.Fragments.Main.OdczytLicznikowFragment;
import wat.learning.android.si.daoprojektsi.R;

public class MainActivity extends AppCompatActivity implements MyResultReceiver.Receiver {

    private int lokatorId;
    private DatabaseConnection dbConn;
    private MyResultReceiver mReceiver;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String jsonConnection = getIntent().getExtras().getString("Connection");
        dbConn = new Gson().fromJson(jsonConnection, DatabaseConnection.class);

        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        lokatorId = getIntent().getIntExtra("id", 0);

        mediaList();
        showButtons();
    }

    public void showButtons(){
        ButtonsFragment buttonsFragment = new ButtonsFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain, buttonsFragment);
        ft.commit();
    }

    public void showOdczyt(){
        OdczytLicznikowFragment odczytLicznikowFragment = new OdczytLicznikowFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain, odczytLicznikowFragment);
        ft.commit();
    }

    public void showOplaty(){
        MiesieczneOplatyFragment miesieczneOplatyFragment = new MiesieczneOplatyFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain, miesieczneOplatyFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
//        if(lokatorId != 0) {
//            Intent logoutIntent = new Intent(this, DatabaseService.class);
//            logoutIntent.putExtra("code", ActionCode.LOGOUT);
//            logoutIntent.putExtra("id", lokatorId);
//            startService(logoutIntent);
//        }
        //TODO: FIX! Logout from db - dziala ale wywala apke
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode){
            case 6:
                list = resultData.getStringArrayList("MediaList");
        }

    }

    private void mediaList(){
        Intent intent = new Intent(this, DatabaseService.class);
        intent.putExtra("receiverTag", mReceiver);
        intent.putExtra("code", ActionCode.GET_MEDIA);
        intent.putExtra("Connection", new Gson().toJson(dbConn));
        startService(intent);
    }

    public ArrayList<String> getMediaList(){
        return list;
    }
}
