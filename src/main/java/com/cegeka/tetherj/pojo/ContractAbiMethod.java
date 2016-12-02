package com.cegeka.tetherj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractAbiMethod implements Serializable {

    private static final long serialVersionUID = 5008090255193587747L;

    private boolean anonymous;
    private boolean constant;
    private String name;
    private AbiVariable[] inputs;
    private AbiVariable[] outputs;
    private String type;

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AbiVariable[] getInputs() {
        return inputs;
    }

    public void setInputs(AbiVariable[] inputs) {
        this.inputs = inputs;
    }

    public AbiVariable[] getOutputs() {
        return outputs;
    }

    public void setOutputs(AbiVariable[] outputs) {
        this.outputs = outputs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
