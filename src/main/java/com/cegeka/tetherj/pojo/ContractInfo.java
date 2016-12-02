package com.cegeka.tetherj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractInfo implements Serializable {

    private static final long serialVersionUID = -9058606692051266438L;

    private String language;
    private String languageVersion;
    private String compilerVersion;
    private String compilerOptions;
    private ContractAbiMethod[] abiDefinition;
    private String source;
    private UserDoc userDoc;
    private DevDoc developerDoc;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageVersion() {
        return languageVersion;
    }

    public void setLanguageVersion(String languageVersion) {
        this.languageVersion = languageVersion;
    }

    public String getCompilerVersion() {
        return compilerVersion;
    }

    public void setCompilerVersion(String compilerVersion) {
        this.compilerVersion = compilerVersion;
    }

    public String getCompilerOptions() {
        return compilerOptions;
    }

    public void setCompilerOptions(String compilerOptions) {
        this.compilerOptions = compilerOptions;
    }

    public ContractAbiMethod[] getAbiDefinition() {
        return abiDefinition;
    }

    public void setAbiDefinition(ContractAbiMethod[] abiDefinition) {
        this.abiDefinition = abiDefinition;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public UserDoc getUserDoc() {
        return userDoc;
    }

    public void setUserDoc(UserDoc userDoc) {
        this.userDoc = userDoc;
    }

    public DevDoc getDeveloperDoc() {
        return developerDoc;
    }

    public void setDeveloperDoc(DevDoc developerDoc) {
        this.developerDoc = developerDoc;
    }
}
