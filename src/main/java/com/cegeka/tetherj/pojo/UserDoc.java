package com.cegeka.tetherj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDoc implements Serializable {

    private static final long serialVersionUID = 7764692830010365466L;

    private UserDocMethods methods;

    public UserDocMethods getMethods() {
        return methods;
    }

    public void setMethods(UserDocMethods methods) {
        this.methods = methods;
    }
}
