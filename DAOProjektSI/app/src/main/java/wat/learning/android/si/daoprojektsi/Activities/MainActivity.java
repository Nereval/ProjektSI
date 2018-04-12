package wat.learning.android.si.daoprojektsi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import wat.learning.android.si.daoprojektsi.ActionCode;
import wat.learning.android.si.daoprojektsi.Database.DatabaseService;
import wat.learning.android.si.daoprojektsi.R;

public class MainActivity extends AppCompatActivity {

    private int lokatorId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lokatorId = getIntent().getIntExtra("id", 0);
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
}
