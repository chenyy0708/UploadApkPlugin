package com.minic.uploadplugin

import javax.inject.Inject

public class PgyerExtension {
    private String apiKey
    private String uKey
    private String password = ""
    private String installType = ""
    @Inject
    public PgyerExtension() {
    }

    String getApiKey() {
        return apiKey
    }

    void setApiKey(String apiKey) {
        this.apiKey = apiKey
    }

    String getuKey() {
        return uKey
    }

    void setuKey(String uKey) {
        this.uKey = uKey
    }

    String getPassword() {
        return password
    }

    void setPassword(String password) {
        this.password = password
    }

    String getInstallType() {
        return installType
    }

    void setInstallType(String installType) {
        this.installType = installType
    }


    @Override
    public String toString() {
        return "PgyerExtension{" +
                "apiKey='" + apiKey + '\'' +
                ", uKey='" + uKey + '\'' +
                ", password='" + password + '\'' +
                ", installType='" + installType + '\'' +
                '}';
    }
}