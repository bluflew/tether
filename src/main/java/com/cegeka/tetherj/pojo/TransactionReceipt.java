package com.cegeka.tetherj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.ethereum.jsonrpc.TransactionReceiptDTO;

import java.io.Serializable;

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

    public TransactionReceipt() {
    }

    public TransactionReceipt(TransactionReceiptDTO txReceipt) {
        this.blockHash = txReceipt.blockHash;
        this.blockNumber = String.valueOf(txReceipt.blockNumber);
        this.contractAddress = txReceipt.contractAddress;
        this.cumulativeGasUsed = String.valueOf(txReceipt.cumulativeGasUsed);
        this.gasUsed = String.valueOf(txReceipt.gasUsed);
        this.logs = txReceipt.logs;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getCumulativeGasUsed() {
        return cumulativeGasUsed;
    }

    public void setCumulativeGasUsed(String cumulativeGasUsed) {
        this.cumulativeGasUsed = cumulativeGasUsed;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public Object[] getLogs() {
        return logs;
    }

    public void setLogs(Object[] logs) {
        this.logs = logs;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
    }
}
