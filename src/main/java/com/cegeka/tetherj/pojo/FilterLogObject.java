package com.cegeka.tetherj.pojo;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterLogObject {
    public FilterLogObject(String filter) {
    	try {
			FilterLogObject obj = new ObjectMapper().readValue(filter, FilterLogObject.class);
			this.type = obj.type;
			this.logIndex = obj.logIndex;
			this.transactionIndex = obj.transactionIndex;
			this.transactionHash = obj.transactionHash;
			this.blockHash = obj.blockHash;
			this.blockNumber = obj.blockNumber;
			this.address = obj.address;
			this.data = obj.data;
			this.topics = obj.topics;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	private String type;
    private String logIndex;
    private String transactionIndex;
    private String transactionHash;
    private String blockHash;
    private String blockNumber;
    private String address;
    private String data;
    private String[] topics;
}
