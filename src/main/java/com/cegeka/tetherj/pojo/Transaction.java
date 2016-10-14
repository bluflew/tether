package com.cegeka.tetherj.pojo;

import java.io.Serializable;

import org.ethereum.jsonrpc.JsonRpc.CallArguments;
import org.ethereum.jsonrpc.TransactionResultDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 5526521386363782072L;

    public String blockHash;
    public String blockNumber;
    public String from;
    public String gas;
    public String gasPrice;
    public String hash;
    public String input;
    public String nonce;
    public String to;
    public String transactionIndex;
    public String value;
    
    public Transaction() { }
    
    public Transaction(TransactionResultDTO tx) {
    	this.blockHash = tx.blockHash;
    	this.blockNumber = tx.blockNumber;
    	this.from = tx.from;
    	this.gas = tx.gas;
    	this.gasPrice = tx.gasPrice;
    	this.hash = tx.hash;
    	this.input = tx.input;
    	this.nonce = tx.nonce;
    	this.to = tx.to;
    	this.transactionIndex = tx.transactionIndex;
    	this.value = tx.value;
    }
    
    public CallArguments toCallArgs() {
    	CallArguments callArgs = new CallArguments();
    	
    	callArgs.from = this.from;
    	callArgs.to = this.to;
    	callArgs.value = this.value;
    	callArgs.nonce = this.nonce;
    	callArgs.gas = this.gas;
    	callArgs.gasPrice = this.gasPrice;
    	
    	return callArgs;
    }
}
