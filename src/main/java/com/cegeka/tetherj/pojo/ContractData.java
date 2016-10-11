package com.cegeka.tetherj.pojo;

import static org.ethereum.jsonrpc.TypeConverter.toJsonHex;

import java.io.IOException;
import java.io.Serializable;

import org.ethereum.solidity.compiler.CompilationResult.ContractMetadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractData implements Serializable {

	private static final long serialVersionUID = -2719743504739845186L;

	String code;
	ContractInfo info;

	public ContractData(ContractMetadata data, String sourceCode, String solcVersion) {
		this.code = toJsonHex(data.bin);

		this.info = new ContractInfo();
		this.info.source = sourceCode;
		this.info.language = "Solidity";
		this.info.languageVersion = "0";
		this.info.compilerVersion = solcVersion;
		
		try {
			this.info.abiDefinition = new ObjectMapper().readValue(data.abi, ContractAbiMethod[].class);
		} catch (IOException e) {
			// Not cool
			throw new RuntimeException(e);
		}
	}
}
