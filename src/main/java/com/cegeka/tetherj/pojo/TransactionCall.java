package com.cegeka.tetherj.pojo;

import java.io.Serializable;

import org.ethereum.jsonrpc.JsonRpc.CallArguments;
import org.ethereum.tether.core.CallTransaction.Function;
import org.springframework.beans.BeanUtils;

import com.cegeka.tetherj.crypto.CryptoUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionCall implements Serializable {

    private static final long serialVersionUID = -478362046262304477L;

    public TransactionCall(Function methodFunction) {
        this.methodFunction = methodFunction;
    }

    public Object[] decodeOutput(String output) {
        return methodFunction.decodeResult(CryptoUtil.hexToBytes(output));
    }
    
    public CallArguments toCallArgs() {
    	CallArguments callArgs = new CallArguments();
    	
    	BeanUtils.copyProperties(this, callArgs);
    	
    	return callArgs;
    }

    private Function methodFunction;
    
    public String from;
    public String to;
    public String gas;
    public String gasPrice;
    public String value;
    public String data;

}
