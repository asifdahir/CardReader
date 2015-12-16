package com.home.intelligentsystems.cardreader.Model;

import java.util.Map;

/**
 * Created by intelligentsystems on 16/12/15.
 */
public class JsonEmployee {
    private Map<String, Employee> descriptor;


    public Map<String, Employee> getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Map<String, Employee> descriptor) {
        this.descriptor = descriptor;
    }
}
