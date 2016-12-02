package com.cegeka.tetherj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DevDocMethod implements Serializable {

    private static final long serialVersionUID = -6621194127186336761L;

    @JsonProperty("return")
    private String returnValue;

    private String details;

    private DevDocMethodParams params;

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public DevDocMethodParams getParams() {
        return params;
    }

    public void setParams(DevDocMethodParams params) {
        this.params = params;
    }
}
