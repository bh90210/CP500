package com.bh90210.cp500;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import cpfiveoo.Cpfiveoo;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        String fb = Cpfiveoo.dbView("fbID"); // Call Go function.
        final TextInputEditText fbID = (TextInputEditText )findViewById(R.id.fbID);
        fbID.clearFocus();
        fbID.setText(fb);
        fbID.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == fbID) {
                    // is only executed if the EditText was directly changed by the user
                    Cpfiveoo.dbUpdate("fbID", String.valueOf(s));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // this space intentionally left blank
            }
        });

        String fbT = Cpfiveoo.dbView("fbTOKEN");
        final TextInputEditText fbTOKEN = (TextInputEditText )findViewById(R.id.fbTOKEN);
        fbTOKEN.clearFocus();
        fbTOKEN.setText(fbT);
        fbTOKEN.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == fbTOKEN) {
                    // is only executed if the EditText was directly changed by the user
                    Cpfiveoo.dbUpdate("fbTOKEN", String.valueOf(s));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // this space intentionally left blank
            }
        });

        String tw1 = Cpfiveoo.dbView("twAPI");
        final TextInputEditText twAPI = (TextInputEditText )findViewById(R.id.twAPI);
        twAPI.clearFocus();
        twAPI.setText(tw1);
        twAPI.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == twAPI) {
                    // is only executed if the EditText was directly changed by the user
                    Cpfiveoo.dbUpdate("twAPI", String.valueOf(s));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // this space intentionally left blank
            }
        });

        String tw2 = Cpfiveoo.dbView("twAPIsec");
        final TextInputEditText twAPIsec = (TextInputEditText )findViewById(R.id.twAPIsec);
        twAPIsec.clearFocus();
        twAPIsec.setText(tw2);
        twAPIsec.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == twAPIsec) {
                    // is only executed if the EditText was directly changed by the user
                    Cpfiveoo.dbUpdate("twAPIsec", String.valueOf(s));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // this space intentionally left blank
            }
        });

        String tw3 = Cpfiveoo.dbView("twTOKEN");
        final TextInputEditText twTOKEN = (TextInputEditText )findViewById(R.id.twTOKEN);
        twTOKEN.setText(tw3);
        twTOKEN.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == twTOKEN) {
                    // is only executed if the EditText was directly changed by the user
                    Cpfiveoo.dbUpdate("twTOKEN", String.valueOf(s));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // this space intentionally left blank
            }
        });

        String tw4 = Cpfiveoo.dbView("twTOKENsec");
        final TextInputEditText twTOKENsec = (TextInputEditText )findViewById(R.id.twTOKENsec);
        twTOKENsec.clearFocus();
        twTOKENsec.setText(tw4);
        twTOKENsec.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == twTOKENsec) {
                    // is only executed if the EditText was directly changed by the user
                    Cpfiveoo.dbUpdate("twTOKENsec", String.valueOf(s));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // this space intentionally left blank
            }
        });

        String inU = Cpfiveoo.dbView("inUSER");
        final TextInputEditText inUSER = (TextInputEditText )findViewById(R.id.inUSER);
        inUSER.clearFocus();
        inUSER.setText(inU);
        inUSER.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == inUSER) {
                    // is only executed if the EditText was directly changed by the user
                    Cpfiveoo.dbUpdate("inUSER", String.valueOf(s));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // this space intentionally left blank
            }
        });

        String inP = Cpfiveoo.dbView("inPASS");
        final TextInputEditText inPASS = (TextInputEditText )findViewById(R.id.inPASS);
        inPASS.clearFocus();
        inPASS.setText(inP);
        inPASS.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == inPASS) {
                    // is only executed if the EditText was directly changed by the user
                    Cpfiveoo.dbUpdate("inPASS", String.valueOf(s));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // this space intentionally left blank
            }
        });
    }
}

