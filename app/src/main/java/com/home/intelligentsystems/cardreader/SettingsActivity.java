package com.home.intelligentsystems.cardreader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by intelligentsystems on 18/12/15.
 */
public class SettingsActivity extends AppCompatActivity {

    EditText editTextServiceURL;
    Button buttonSave;
    Button buttonCancel;

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.settings);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        setupActionBar();

        editTextServiceURL = (EditText) findViewById(R.id.edit_service_url);

        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(Common.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
                editor.putString(Common.SHARED_PREFERENCES_KEY_SERVICE_URL, editTextServiceURL.getText().toString());
                editor.commit();
                finish();
            }
        });

        buttonCancel = (Button) findViewById(R.id.button_cacnel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(Common.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Common.SERVICE_URL = sharedPreferences.getString(Common.SHARED_PREFERENCES_KEY_SERVICE_URL, "");
        editTextServiceURL.setText(Common.SERVICE_URL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
