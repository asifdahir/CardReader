package com.home.intelligentsystems.cardreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.home.intelligentsystems.cardreader.Model.Employee;

import java.io.File;

public class MainActivity extends AppCompatActivity implements TaskDelegate {

    Button buttonScanCard;
    Button buttonAuthenticate;
    TextView textViewAuthenticationResult;
    TextView textViewCard;
    ImageView imageView;

    IntentResult mIntentResult;

    private boolean isEmployeeAuthorized() {

        WebAPIHandler webAPIHandler = new WebAPIHandler(this);
        webAPIHandler.execute(Common.SERVICE_URL);

        String contents = mIntentResult.getContents();
        if (contents.equals("8961006010078")) {
            return true;
        }
        return false;
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();

        buttonScanCard = (Button) findViewById(R.id.button_scan_card);
        buttonScanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
                integrator.setOrientationLocked(false);
                integrator.setPrompt("Scan Card");
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        buttonAuthenticate = (Button) findViewById(R.id.button_authenticate);
        buttonAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIntentResult == null) {
                    Toast.makeText(MainActivity.this, "Please scan card", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isEmployeeAuthorized()) {
                    textViewAuthenticationResult.setTextColor(getResources().getColor(R.color.colorGreen));
                    textViewAuthenticationResult.setText("AUTHORIZED");
                } else {
                    textViewAuthenticationResult.setTextColor(getResources().getColor(R.color.colorRed));
                    textViewAuthenticationResult.setText("UN-AUTHORIZED");
                }
            }
        });

        textViewCard = (TextView) findViewById(R.id.text_card);
        textViewAuthenticationResult = (TextView) findViewById(R.id.text_authentication_result);
        imageView = (ImageView) findViewById(R.id.image_scan);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
            return;

        File file;
        Bitmap bitmap;
        String result;

        mIntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        //Toast.makeText(this,mIntentResult.getContents(),Toast.LENGTH_LONG).show();
        result = String.format("Content: %s\nFormat: %s", mIntentResult.getContents(), mIntentResult.getFormatName());
        textViewCard.setText(result);

        file = new File(mIntentResult.getBarcodeImagePath());
        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }

        textViewAuthenticationResult.setText("");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(Common.CARD_CONTENT, textViewCard.getText().toString());

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textViewCard.setText(savedInstanceState.getString(Common.CARD_CONTENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void taskCompletionResult(Employee employee) {
        Toast.makeText(this, employee.toString(), Toast.LENGTH_SHORT).show();
    }
}
