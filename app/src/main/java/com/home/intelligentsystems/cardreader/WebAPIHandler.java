package com.home.intelligentsystems.cardreader;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.home.intelligentsystems.cardreader.Model.Employee;
import com.home.intelligentsystems.cardreader.Model.JsonEmployee;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

/**
 * Created by intelligentsystems on 16/12/15.
 */
public class WebAPIHandler extends AsyncTask<String, String, Employee> {

    private TaskDelegate delegate;

    public WebAPIHandler(TaskDelegate taskDelegate) {
        this.delegate = taskDelegate;
    }

    @Override
    protected Employee doInBackground(String... params) {
        String urlString = params[0];
        InputStream in = null;
        Employee employee = null;

        // HTTP Get
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (Exception e) {
            Log.d(Common.TAG, e.getMessage(), e);
            return employee;
        }

        try {
            byte[] buff = new byte[32];
            int dataRead = -1;
            StringBuilder stringBuilder = new StringBuilder();
            while ((dataRead = in.read(buff)) > 0) {
                stringBuilder.append(new String(buff, 0, dataRead));
            }
            in.close();

            Log.d(Common.TAG, stringBuilder.toString());

            Gson gson = new Gson();
            employee = gson.fromJson(stringBuilder.toString(), Employee.class);
            employee.setPhotoData();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return employee;
    }

    protected void onPostExecute(Employee employee) {
        delegate.taskCompletionResult(employee);
    }
}
