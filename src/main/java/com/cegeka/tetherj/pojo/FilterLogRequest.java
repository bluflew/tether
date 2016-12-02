package com.cegeka.tetherj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.ethereum.jsonrpc.JsonRpc.FilterRequest;
import org.ethereum.tether.core.CallTransaction.Function;

import java.io.Serializable;
import java.math.BigInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterLogRequest implements Serializable {

    public static final FilterLogRequest DEFAULT = new FilterLogRequest();
    private static final long serialVersionUID = 4840490358731008368L;
    @JsonIgnore
    Function function;
    private String fromBlock;
    private String toBlock;
    private String address;
    private String[] topics;

    /**
     * Decode data from filter log objects.
     *
     * @param data to decode.
     * @return array of result objects.
     */
    public Object[] decodeEventData(String data, String[] topics) {
        if (function != null) {
            return function.decodeEventData(data, topics);
        }

        return null;
    }

    public String getFromBlock() {
        return fromBlock;
    }

    public void setFromBlock(BigInteger fromBlock) {
        setFromBlock("0x" + fromBlock.toString(16));
    }

    public void setFromBlock(String fromBlock) {
        this.fromBlock = fromBlock;
    }

    public String getToBlock() {
        return toBlock;
    }

    public void setToBlock(BigInteger toBlock) {
        setToBlock("0x" + toBlock.toString(16));
    }

    public void setToBlock(String toBlock) {
        this.toBlock = toBlock;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }

    public FilterRequest toFilterRequest() {
        FilterRequest req = new FilterRequest();

        req.fromBlock = this.fromBlock;
        req.toBlock = this.toBlock;
        req.address = this.address;
        req.topics = this.topics;

        return req;

    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }
}
