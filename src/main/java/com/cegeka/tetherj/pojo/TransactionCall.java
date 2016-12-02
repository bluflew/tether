package com.cegeka.tetherj.pojo;

import com.cegeka.tetherj.crypto.CryptoUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.ethereum.jsonrpc.JsonRpc.CallArguments;
import org.ethereum.tether.core.CallTransaction.Function;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionCall implements Serializable {

    private static final long serialVersionUID = -478362046262304477L;

    private Function methodFunction;

    private String from;
    private String to;
    private String gas;
    private String gasPrice;
    private String value;
    private String data;

    public TransactionCall(Function methodFunction) {
        this.methodFunction = methodFunction;
    }

    public Object[] decodeOutput(String output) {
        return methodFunction.decodeResult(CryptoUtil.hexToBytes(output));
    }

    public CallArguments toCallArgs() {
        CallArguments callArgs = new CallArguments();

        callArgs.from = this.from;
        callArgs.to = this.to;
        callArgs.gas = this.gas;
        callArgs.gasPrice = this.gasPrice;
        callArgs.value = this.value;
        callArgs.data = this.data;

        return callArgs;
    }

    public Function getMethodFunction() {
        return methodFunction;
    }

    public void setMethodFunction(Function methodFunction) {
        this.methodFunction = methodFunction;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
