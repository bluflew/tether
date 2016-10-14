package com.cegeka.tetherj.pojo;

import java.io.Serializable;

import org.ethereum.jsonrpc.TransactionReceiptDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionReceipt implements Serializable {

	private static final long serialVersionUID = 3551140643531052025L;

    private String blockHash;
    private String blockNumber;
    private String contractAddress;
    private String cumulativeGasUsed;
    private String gasUsed;
    private Object[] logs;
    private String transactionHash;
    private String transactionIndex;
    
    public TransactionReceipt() { }
    
    public TransactionReceipt(TransactionReceiptDTO txReceipt) {
    	this.blockHash = txReceipt.blockHash;
    	this.blockNumber = String.valueOf(txReceipt.blockNumber);
    	this.contractAddress = txReceipt.contractAddress;
    	this.cumulativeGasUsed = String.valueOf(txReceipt.cumulativeGasUsed);
    	this.gasUsed = String.valueOf(txReceipt.gasUsed);
    	this.logs = txReceipt.logs;
    }
}
