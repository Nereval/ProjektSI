package wat.learning.android.si.daoprojektsi.Fragments.Login;


import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import wat.learning.android.si.daoprojektsi.Activities.LoginActivity;
import wat.learning.android.si.daoprojektsi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {

    private View fragView;
    private EditText pass1, pass2;
    private String field1, field2;
    private Button resetButton;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.fragment_reset_password, container, false);

        pass1 = fragView.findViewById(R.id.pass1);
        pass2 = fragView.findViewById(R.id.pass2);
        resetButton = fragView.findViewById(R.id.reset_password_button);

        pass1.setError(null);
        pass2.setError(null);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View focusView = null;
                boolean cancel = false;

                field1 = pass1.getText().toString();
                field2 = pass2.getText().toString();

                if (TextUtils.isEmpty(field1) || !isPasswordValid(field1)) {
                    pass1.setError(getString(R.string.error_invalid_password));
                    focusView = pass1;
                    cancel = true;
                }

                if (TextUtils.isEmpty(field2) || !isPasswordValid(field2)) {
                    pass1.setError(getString(R.string.error_invalid_password));
                    focusView = pass2;
                    cancel = true;
                }

                if(!field1.equals(field2)){
                    pass1.setError(getString(R.string.passwords_do_not_match));
                    pass2.setError(getString(R.string.passwords_do_not_match));
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                    cancel = false;
                } else {
                    ((LoginActivity) getActivity()).resetPassword(field2);
                }
            }
        });
        return fragView;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
