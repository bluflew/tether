package com.cegeka.tetherj.pojo;

import java.io.IOException;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterLogObject {
    public FilterLogObject(String filter) {
    	try {
			FilterLogObject obj = new ObjectMapper().readValue(filter, FilterLogObject.class);
			BeanUtils.copyProperties(obj, this);
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
