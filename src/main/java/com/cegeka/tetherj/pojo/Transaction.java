package com.cegeka.tetherj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.ethereum.jsonrpc.JsonRpc.CallArguments;
import org.ethereum.jsonrpc.TransactionResultDTO;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 5526521386363782072L;

    private String blockHash;
    private String blockNumber;
    private String from;
    private String gas;
    private String gasPrice;
    private String hash;
    private String input;
    private String nonce;
    private String to;
    private String transactionIndex;
    private String value;

    public Transaction() {
    }

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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
