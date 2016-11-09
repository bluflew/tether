package com.cegeka.tetherj.pojo;

import org.ethereum.jsonrpc.JsonRpc.BlockResult;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {
	public String number;
    public String hash;
    public String parentHash;
    public String nonce;
    public String sha3Uncles;
    public String logsBloom;
    public String transactionsRoot;
    public String stateRoot;
    public String receiptRoot;
    public String miner;
    public String difficulty;
    public String totalDifficulty;
    public String extraData;
    public String size;
    public String gasLimit;
    public String gasUsed;
    public String timestamp;
    public String[] transactions;
    public String[] uncles;
    
    public Block() {
      // Needed for Jackson
    }
    
    public Block(BlockResult block) {
    	this.number = block.number;
    	this.hash = block.hash;
    	this.parentHash = block.parentHash;
    	this.nonce = block.nonce;
    	this.sha3Uncles = block.sha3Uncles;
    	this.logsBloom = block.logsBloom;
    	this.transactionsRoot = block.transactionsRoot;
    	this.stateRoot = block.stateRoot;
    	this.receiptRoot = block.receiptsRoot;
    	this.miner = block.miner;
    	this.difficulty = block.difficulty;
    	this.totalDifficulty = block.totalDifficulty;
    	this.extraData = block.extraData;
    	this.size = block.size;
    	this.gasLimit = block.gasLimit;
    	this.gasUsed = block.gasUsed;
    	this.timestamp = block.timestamp;
    	this.transactions = (String [])block.transactions;
    	this.uncles = block.uncles;
    }
}
