package wat.learning.android.si.daoprojektsi.Fragments.Login;


import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import wat.learning.android.si.daoprojektsi.Activities.LoginActivity;
import wat.learning.android.si.daoprojektsi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    private View fragView;
    private TextView mEmailView;
    private EditText mPasswordView;
    private String password;
    private String email;
    private boolean cancel = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.fragment_login, container, false);

        mEmailView = (TextView) fragView.findViewById(R.id.email);
        mPasswordView = (EditText) fragView.findViewById(R.id.password);

        mEmailView.setError(null);
        mPasswordView.setError(null);

            Button mEmailSignInButton = (Button) fragView.findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    View focusView = null;
                    email = mEmailView.getText().toString();
                    password = mPasswordView.getText().toString();

                    if (TextUtils.isEmpty(email)) {
                        mEmailView.setError(getString(R.string.error_field_required));
                        focusView = mEmailView;
                        cancel = true;
                    } else if (!isEmailValid(email)) {
                        mEmailView.setError(getString(R.string.error_invalid_email));
                        focusView = mEmailView;
                        cancel = true;
                    }

                    if (TextUtils.isEmpty(password)) {
                        mEmailView.setError(getString(R.string.error_field_required));
                        focusView = mPasswordView;
                        cancel = true;
                    } else if (!isPasswordValid(password)) {
                        mPasswordView.setError(getString(R.string.error_invalid_password));
                        focusView = mPasswordView;
                        cancel = true;
                    }

                    if (cancel) {
                        focusView.requestFocus();
                        cancel = false;
                    } else {
                        ((LoginActivity) getActivity()).attemptLogin(email, password);
                    }
                }
            });
        return fragView;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
