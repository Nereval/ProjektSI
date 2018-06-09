package wat.learning.android.si.daoprojektsi.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import wat.learning.android.si.daoprojektsi.ActionCode;
import wat.learning.android.si.daoprojektsi.Database.DatabaseConnection;
import wat.learning.android.si.daoprojektsi.Database.DatabaseService;
import wat.learning.android.si.daoprojektsi.Database.MyResultReceiver;
import wat.learning.android.si.daoprojektsi.Fragments.Login.ProgressFragment;
import wat.learning.android.si.daoprojektsi.Fragments.Main.ButtonsFragment;
import wat.learning.android.si.daoprojektsi.Fragments.Main.MiesieczneOplatyV2Fragment;
import wat.learning.android.si.daoprojektsi.Fragments.Main.NowaWiadomoscFragment;
import wat.learning.android.si.daoprojektsi.Fragments.Main.OdczytLicznikowFragment;
import wat.learning.android.si.daoprojektsi.R;

import static wat.learning.android.si.daoprojektsi.Activities.LoginActivity.hideKeyboard;

public class MainActivity extends AppCompatActivity implements MyResultReceiver.Receiver {

    private static final int LISTA_MEDIÓW = 6;
    private static final int MIESIĘCZNE_OPŁATY = 7;

    private int lokatorId;
    private DatabaseConnection dbConn;
    private MyResultReceiver mReceiver;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<ArrayList<String>> miesięczneOpłaty = new ArrayList<>();

//    private String[] titles;
//    private ListView drawerList;
//    private DrawerLayout drawerLayout;
//    private ActionBarDrawerToggle drawerToggle;
//
//    private class DrawerItemClickListener implements ListView.OnItemClickListener{
//
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            selectItem(i);
//        }
//    }
//
//    private void selectItem(int position){
//        Fragment fragment;
//        switch (position){
//            case 0:
//                fragment = new OdczytLicznikowFragment();
//                break;
//            case 1:
//                fragment = new MiesieczneOplatyV2Fragment();
//                break;
//            case 2:
//                fragment = new NowaWiadomoscFragment();
//                break;
//            default:
//                fragment = new OdczytLicznikowFragment();
//        }
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(R.id.frameMain, fragment);
//        ft.addToBackStack(null);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        ft.commit();
//
//        drawerLayout.closeDrawer(drawerList);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String jsonConnection = getIntent().getExtras().getString("Connection");
        dbConn = new Gson().fromJson(jsonConnection, DatabaseConnection.class);

        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        lokatorId = getIntent().getIntExtra("id", 0);

//        titles = getResources().getStringArray(R.array.menu);
//        drawerList = (ListView)findViewById(R.id.drawer);
//        drawerList.setAdapter(new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_activated_1, titles));
//        drawerList.setOnItemClickListener(new DrawerItemClickListener());
//
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if(savedInstanceState == null){
//            selectItem(0);
//        }
//
//        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
//                R.string.open_drawer, R.string.close_drawer){
//            @Override
//            public void onDrawerClosed(View view){
//                super.onDrawerClosed(view);
//                invalidateOptionsMenu();
//            }
//
//            @Override
//            public void onDrawerOpened(View view){
//                super.onDrawerOpened(view);
//                invalidateOptionsMenu();
//            }
//        };
//        drawerLayout.setDrawerListener(drawerToggle);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);

        mediaList();
        showButtons();
    }

    public void showNowaWiadomosc() {
        NowaWiadomoscFragment nowaWiadomoscFragment = new NowaWiadomoscFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain, nowaWiadomoscFragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        drawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        drawerToggle.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        if(drawerToggle.onOptionsItemSelected(item))
//            return true;
//        return false;
//    }

    public void showButtons() {
        ButtonsFragment buttonsFragment = new ButtonsFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain, buttonsFragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void showOdczyt() {
        OdczytLicznikowFragment odczytLicznikowFragment = new OdczytLicznikowFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain, odczytLicznikowFragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void showOplaty() {
        MiesieczneOplatyV2Fragment miesieczneOplatyFragment = new MiesieczneOplatyV2Fragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain, miesieczneOplatyFragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void dodajOdczyt(String odczyt, String dataOdczytu) {
        //TODO: co ma robić po dodaniu
    }


    public void miesięczneOpłaty(String miesiacS, String rokS) {
        showProgress();

        Intent intent = new Intent(this, DatabaseService.class);
        intent.putExtra("receiverTag", mReceiver);
        intent.putExtra("code", ActionCode.MIESIĘCZNE_OPŁATY);
        intent.putExtra("id", lokatorId);
        intent.putExtra("Connection", new Gson().toJson(dbConn));
        intent.putExtra("miesiac", miesiacS);
        intent.putExtra("rok", rokS);
        startService(intent);

    }
//    @Override
//    public void onBackPressed() {
////        if(lokatorId != 0) {
////            Intent logoutIntent = new Intent(this, DatabaseService.class);
////            logoutIntent.putExtra("code", ActionCode.LOGOUT);
////            logoutIntent.putExtra("id", lokatorId);
////            startService(logoutIntent);
////        }
//        //TODO: FIX! Logout from db - dziala ale wywala apke
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
//        System.exit(0);
//    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
            case LISTA_MEDIÓW:
                list = resultData.getStringArrayList("MediaList");
                break;
            case MIESIĘCZNE_OPŁATY:
                String json = resultData.getString("miesięczneOpłaty");
                miesięczneOpłaty = new Gson().fromJson(json, new TypeToken<ArrayList<List<String>>>() {
                }.getType());
                showOplaty();
        }

    }

    private void mediaList() {
        Intent intent = new Intent(this, DatabaseService.class);
        intent.putExtra("receiverTag", mReceiver);
        intent.putExtra("code", ActionCode.GET_MEDIA);
        intent.putExtra("Connection", new Gson().toJson(dbConn));
        startService(intent);
    }

    public ArrayList<String> getMediaList() {
        return list;
    }

    public ArrayList<ArrayList<String>> getMiesięczneOpłaty() {
        return miesięczneOpłaty;
    }


    public void showProgress() {
        hideKeyboard(this);
        ProgressFragment progressFragment = new ProgressFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain, progressFragment);
        ft.commit();
    }
}