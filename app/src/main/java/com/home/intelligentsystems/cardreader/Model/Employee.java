package com.home.intelligentsystems.cardreader.Model;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by intelligentsystems on 16/12/15.
 */

public class Employee {
    private int Id;
    private String Name;
    private String Phone1;
    private String Phone2;
    private String Address;
    private String Photo;
    private byte[] PhotoBytes;

    public void setPhotoData() throws UnsupportedEncodingException {
        PhotoBytes = Base64.decode(Photo, Base64.DEFAULT);
    }

    public String toString()
    {
        return Name;
    }
}
