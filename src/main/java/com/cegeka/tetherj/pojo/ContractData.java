package com.cegeka.tetherj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ethereum.solidity.compiler.CompilationResult.ContractMetadata;

import java.io.IOException;
import java.io.Serializable;

import static org.ethereum.jsonrpc.TypeConverter.toJsonHex;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractData implements Serializable {

    private static final long serialVersionUID = -2719743504739845186L;

    private String code;
    private ContractInfo info;

    public ContractData() {
    }

    public ContractData(ContractMetadata data, String sourceCode, String solcVersion) {
        this.code = toJsonHex(data.bin);

        this.info = new ContractInfo();
        this.info.setSource(sourceCode);
        this.info.setLanguage("Solidity");
        this.info.setLanguageVersion("0");
        this.info.setCompilerVersion(solcVersion);

        try {
            this.info.setAbiDefinition(new ObjectMapper().readValue(data.abi, ContractAbiMethod[].class));
        } catch (IOException e) {
            // Not cool
            throw new RuntimeException(e);
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ContractInfo getInfo() {
        return info;
    }

    public void setInfo(ContractInfo info) {
        this.info = info;
    }
}
