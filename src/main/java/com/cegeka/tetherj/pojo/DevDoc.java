package com.cegeka.tetherj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DevDoc implements Serializable {

    private static final long serialVersionUID = 297037653570409555L;

    private String title;
    private DevDocMethods methods;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DevDocMethods getMethods() {
        return methods;
    }

    public void setMethods(DevDocMethods methods) {
        this.methods = methods;
    }
}
