package com.cegeka.tetherj.pojo;

import java.io.Serializable;
import java.math.BigInteger;

import org.ethereum.jsonrpc.JsonRpc.FilterRequest;
import org.ethereum.tether.core.CallTransaction.Function;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterLogRequest implements Serializable {

    private static final long serialVersionUID = 4840490358731008368L;
    public static final FilterLogRequest DEFAULT = new FilterLogRequest();

    String fromBlock;
    String toBlock;
    String address;
    String[] topics;

    @JsonIgnore
    Function function;

    /**
     * Decode data from filter log objects.
     * @param data to decode.
     * @return array of result objects.
     */
    public Object[] decodeEventData(String data, String[] topics) {
        if (function != null) {
            return function.decodeEventData(data, topics);
        }

        return null;
    }

    public void setFromBlock(String fromBlock) {
        this.fromBlock = fromBlock;
    }

    public void setFromBlock(BigInteger fromBlock) {
        setFromBlock("0x" + fromBlock.toString(16));
    }

    public void setToBlock(String toBlock) {
        this.toBlock = toBlock;
    }

    public void setToBlock(BigInteger toBlock) {
        setToBlock("0x" + toBlock.toString(16));
    }

	public FilterRequest toFilterRequest() {
		FilterRequest req = new FilterRequest();
		
		BeanUtils.copyProperties(this, req);
		
		return req;
		
	}
}
