package com.home.intelligentsystems.cardreader.Model;

import android.util.Base64;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by intelligentsystems on 16/12/15.
 */

public class Employee implements Serializable{
    private int Id;
    private String Name;
    private String Phone1;
    private String Phone2;
    private String Address;
    private String Photo;
    private byte[] PhotoBytes;

    public void setPhotoData() throws UnsupportedEncodingException {
        setPhotoBytes(Base64.decode(Photo, Base64.DEFAULT));
    }

    public String toString()
    {
        return getName();
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone1() {
        return Phone1;
    }

    public void setPhone1(String phone1) {
        Phone1 = phone1;
    }

    public String getPhone2() {
        return Phone2;
    }

    public void setPhone2(String phone2) {
        Phone2 = phone2;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public byte[] getPhotoBytes() {
        return PhotoBytes;
    }

    public void setPhotoBytes(byte[] photoBytes) {
        PhotoBytes = photoBytes;
    }
}
