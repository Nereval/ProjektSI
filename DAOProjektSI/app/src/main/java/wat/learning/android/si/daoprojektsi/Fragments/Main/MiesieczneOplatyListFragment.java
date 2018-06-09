package wat.learning.android.si.daoprojektsi.Fragments.Main;


import android.os.Bundle;
import android.app.Fragment;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import wat.learning.android.si.daoprojektsi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MiesieczneOplatyListFragment extends ListFragment {

    public ArrayList<ArrayList<String>> oplaty = new ArrayList<>();

    public MiesieczneOplatyListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        oplaty = ((MiesieczneOplatyV2Fragment)getParentFragment()).oplaty;

        ArrayAdapter<ArrayList<String>> adapter = new ArrayAdapter<>(
                inflater.getContext(), android.R.layout.simple_list_item_1,oplaty
        );
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
