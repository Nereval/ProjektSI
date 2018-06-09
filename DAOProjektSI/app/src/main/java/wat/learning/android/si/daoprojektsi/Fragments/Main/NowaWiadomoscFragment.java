package wat.learning.android.si.daoprojektsi.Fragments.Main;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import wat.learning.android.si.daoprojektsi.Activities.MainActivity;
import wat.learning.android.si.daoprojektsi.MyAdapter;
import wat.learning.android.si.daoprojektsi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowaWiadomoscFragment extends Fragment {

    public View fragView;
    public ArrayList<String> zalaczniki = new ArrayList<String>();

    public NowaWiadomoscFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.fragment_nowa_wiadomosc, container, false);

        //test
//        zalaczniki.add("Item1");
//        zalaczniki.add("Item2");

        MyAdapter adapter = new MyAdapter(zalaczniki, this.getActivity());
        final ListView lView = (ListView)fragView.findViewById(R.id.zalaczniki);
        lView.setAdapter(adapter);

        Button wyslij = (Button)fragView.findViewById(R.id.wyslij);
        wyslij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Co się dzieje po wciśnięciu przycisku wyślij
                EditText odbiorca = (EditText) fragView.findViewById(R.id.odbiorca);
                odbiorca.clearComposingText();
            }
        });

        Button dodajZalacznik = (Button)fragView.findViewById(R.id.dodaj_zalacznik);
        dodajZalacznik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Co się dzieje po wciśnięciu przycisku dodajZalacznik (Eksplorer plików)
                zalaczniki.add("Item1");
            }
        });

        return fragView;
    }

}
