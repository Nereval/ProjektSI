package wat.learning.android.si.daoprojektsi.Fragments.Main;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import wat.learning.android.si.daoprojektsi.Activities.MainActivity;
import wat.learning.android.si.daoprojektsi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OdczytLicznikowFragment extends Fragment {

    private List<String> list = new ArrayList<>();
    private View fragView;
    private String chosenMedia;

    public OdczytLicznikowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.fragment_odczyt_licznikow, container, false);

        list = ((MainActivity)getActivity()).getMediaList();

        Spinner sMedia = fragView.findViewById(R.id.spinnerOdczyt);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(fragView.getContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMedia.setAdapter(adapter);

        Button dodaj = (Button) fragView.findViewById(R.id.dodaj_odczyt);
        dodaj.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText odczytET = (EditText) fragView.findViewById(R.id.wartosc_odczytu);
                EditText dataOdczytuET = (EditText) fragView.findViewById(R.id.data_odczytu);

                String odczyt = odczytET.getText().toString();
                String dataOdczytu = dataOdczytuET.getText().toString();

                ((MainActivity)getActivity()).dodajOdczyt(odczyt, dataOdczytu);
            }
        });

        return fragView;
    }

}
