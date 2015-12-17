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

    WebAPIHandler webAPIHandler;
    IntentResult intentResult;
    Button buttonScanCard;
    Button buttonAuthenticate;
    TextView textViewCard;
    ImageView imageView;

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
                if (intentResult == null) {
                    Toast.makeText(MainActivity.this, "Please scan card", Toast.LENGTH_SHORT).show();
                    return;
                }
                webAPIHandler = new WebAPIHandler(MainActivity.this);
                webAPIHandler.execute(intentResult.getContents());
            }
        });


        textViewCard = (TextView) findViewById(R.id.text_card_id);
        imageView = (ImageView) findViewById(R.id.image_card_scan);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
            return;

        File file;
        Bitmap bitmap;
        String result;

        intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        //Toast.makeText(this,intentResult.getContents(),Toast.LENGTH_LONG).show();
        //result = String.format("Content: %s\nFormat: %s", intentResult.getContents(), intentResult.getFormatName());
        result = String.format("Card ID: %s", intentResult.getContents());
        textViewCard.setText(result);

        file = new File(intentResult.getBarcodeImagePath());
        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(Common.KEY_CARD_CONTENT, textViewCard.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textViewCard.setText(savedInstanceState.getString(Common.KEY_CARD_CONTENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void taskCompletionResult(Employee employee) {

        WebAPIOperationResult operationResult;
        Intent intent;

        if (webAPIHandler == null) return;

        operationResult = webAPIHandler.getOperationResult();
        switch (operationResult) {
            case COMPLETED:
            case FILE_NOT_FOUND:
                intent = new Intent(this, EmployeeActivity.class);
                intent.putExtra(Common.KEY_EMPLOYEE, employee);
                startActivity(intent);
                break;
            case CONNECTION_ERROR:
                Toast.makeText(this, "Connection to server failed", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_IN_READING_DATA:
                Toast.makeText(this, "Error in reading data from server", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
