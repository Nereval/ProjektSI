package wat.learning.android.si.daoprojektsi.Fragments.Main;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;

import wat.learning.android.si.daoprojektsi.ActionCode;
import wat.learning.android.si.daoprojektsi.Activities.MainActivity;
import wat.learning.android.si.daoprojektsi.Database.DatabaseService;
import wat.learning.android.si.daoprojektsi.Fragments.Login.ProgressFragment;
import wat.learning.android.si.daoprojektsi.R;

import static wat.learning.android.si.daoprojektsi.Activities.LoginActivity.hideKeyboard;

public class MiesieczneOplatyV2Fragment extends Fragment {

    public View fragView;
    public ArrayList<ArrayList<String>> oplaty = new ArrayList<>();
    private String miesiacS, rokS;

    public MiesieczneOplatyV2Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.fragment_miesieczne_oplaty_v2, container, false);

        oplaty = ((MainActivity)getActivity()).getMiesięczneOpłaty();
        //TODO: Dane wyciągnięte i wyświetlone - poprawić wyświtlanie
        if(!oplaty.isEmpty()){
            Fragment fragment = new MiesieczneOplatyListFragment();
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.list_fragment, fragment);
            ft.commit();
        }

        Button button = (Button)fragView.findViewById(R.id.pokaz_oplaty);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pokazMiesieczneOplaty();
            }
        });

        return fragView;
    }

    public void pokazMiesieczneOplaty(){
        Spinner miesiac = (Spinner)fragView.findViewById(R.id.miesiac);
        miesiacS = miesiac.getSelectedItem().toString();
        miesiacS = miesiacS.replace("\"", "");

        Spinner rok = (Spinner)fragView.findViewById(R.id.rok);
        rokS = rok.getSelectedItem().toString();
        rokS = rokS.replace("\"", "");

        ((MainActivity)getActivity()).miesięczneOpłaty(miesiacS, rokS);
    }

}
