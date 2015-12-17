package com.home.intelligentsystems.cardreader;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.home.intelligentsystems.cardreader.Model.Employee;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by intelligentsystems on 16/12/15.
 */
public class WebAPIHandler extends AsyncTask<String, String, Employee> {

    private TaskDelegate delegate;
    private WebAPIOperationResult operationResult;

    public WebAPIOperationResult getOperationResult() {
        return operationResult;
    }

    public WebAPIHandler(TaskDelegate taskDelegate) {
        this.delegate = taskDelegate;
    }

    @Override
    protected Employee doInBackground(String... params) {
        URL url;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        Employee employee = null;
        Gson gson;
        StringBuilder stringBuilder;
        String urlString = params[0];
        int dataRead;
        byte[] buff;

        try {
            url = new URL(Common.SERVICE_URL + urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
        } catch (FileNotFoundException fileNotFoundException) {
            operationResult = WebAPIOperationResult.FILE_NOT_FOUND;
            return employee;
        } catch (Exception e) {
            operationResult = WebAPIOperationResult.CONNECTION_ERROR;
            Log.d(Common.TAG, e.getMessage(), e);
            return employee;
        }

        try {
            buff = new byte[32];
            stringBuilder = new StringBuilder();
            while ((dataRead = inputStream.read(buff)) > 0) {
                stringBuilder.append(new String(buff, 0, dataRead));
            }
            inputStream.close();

            Log.d(Common.TAG, stringBuilder.toString());

            gson = new Gson();
            employee = gson.fromJson(stringBuilder.toString(), Employee.class);
            employee.setPhotoData();

            operationResult = WebAPIOperationResult.COMPLETED;

        } catch (IOException e) {
            operationResult = WebAPIOperationResult.ERROR_IN_READING_DATA;
            Log.d(Common.TAG, e.getMessage(), e);
        }
        return employee;
    }

    protected void onPostExecute(Employee employee) {
        delegate.taskCompletionResult(employee);
    }
}
