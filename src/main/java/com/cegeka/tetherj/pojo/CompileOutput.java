package com.cegeka.tetherj.pojo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.ethereum.solidity.compiler.CompilationResult;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompileOutput {
    private HashMap<String, ContractData> contractData;

    public CompileOutput() {
        contractData = new HashMap<>();
    }

    public CompileOutput(CompilationResult result, String sourceCode) {
        this();

        result.contracts.forEach((name, data) -> {
            contractData.put(name, new ContractData(data, sourceCode, result.version));
        });
    }

    @JsonAnySetter
    public void setDynamicContractData(String name, ContractData contract) {
        contractData.put(name, contract);
    }

    @JsonAnyGetter
    public HashMap<String, ContractData> getContractData() {
        return contractData;
    }

    /**
     * Get all contract names in this compilation output.
     *
     * @return names
     */
    public String[] getContractNames() {
        String[] names = new String[contractData.keySet().size()];
        contractData.keySet().toArray(names);
        return names;
    }

    public ContractData getContractByName(String name) {
        return contractData.get(name);
    }
}
