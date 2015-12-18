package com.home.intelligentsystems.cardreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.home.intelligentsystems.cardreader.Model.Employee;

/**
 * Created by intelligentsystems on 17/12/15.
 */
public class EmployeeActivity extends AppCompatActivity {

    Employee employee;
    TextView textViewAuthenticationResult;
    ImageView imageView;
    TextView textViewName;
    TextView textViewPhone1;
    TextView textViewAddress;

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_employee);

        setupActionBar();

        textViewAuthenticationResult = (TextView) findViewById(R.id.text_authentication_result);
        imageView = (ImageView) findViewById(R.id.image_employee);
        textViewName = (TextView) findViewById(R.id.text_employee_name);
        textViewPhone1 = (TextView) findViewById(R.id.text_employee_phone1);
        textViewAddress = (TextView) findViewById(R.id.text_employee_address);

        employee = (Employee) getIntent().getSerializableExtra(Common.KEY_EMPLOYEE);
        if (employee == null) {
            textViewAuthenticationResult.setTextColor(getResources().getColor(R.color.colorRed));
            textViewAuthenticationResult.setText("UN-AUTHORIZED EMPLOYEE");
        } else {
            textViewAuthenticationResult.setTextColor(getResources().getColor(R.color.colorGreen));
            textViewAuthenticationResult.setText("EMPLOYEE AUTHORIZED");

            Bitmap bitmap = BitmapFactory.decodeByteArray(employee.getPhotoBytes(), 0, employee.getPhotoBytes().length);
            imageView.setImageBitmap(bitmap);

            textViewName.setText("Name: " + employee.getName());
            textViewPhone1.setText("Phone: " + employee.getPhone1());
            textViewAddress.setText("Address: " + employee.getAddress());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
