package com.home.intelligentsystems.cardreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button buttonScanCard;
    Button buttonAuthenticate;
    TextView textViewAuthenticationResult;
    TextView textViewCard;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                if(true)
                {
                    textViewAuthenticationResult.setTextColor(getResources().getColor(R.color.colorDarkGreen));
                    textViewAuthenticationResult.setText("AUTHORIZED");
                }
                else
                {

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
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        //Toast.makeText(this,intentResult.getContents(),Toast.LENGTH_LONG).show();
        String result = String.format("Content: %s\nFormat: %s",
                intentResult.getContents(),
                intentResult.getFormatName());
        textViewCard.setText(result);

        File imgFile = new  File(intentResult.getBarcodeImagePath());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
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
}
