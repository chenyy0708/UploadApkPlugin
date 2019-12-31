package com.minic.uploadplugin

import javax.inject.Inject

public class FirExtension {
    private String appName
    private String iconPath
    private String token

    @Inject
    public FirExtension() {
    }

    String getAppName() {
        return appName
    }

    void setAppName(String appName) {
        this.appName = appName
    }

    String getIconPath() {
        return iconPath
    }

    void setIconPath(String iconPath) {
        this.iconPath = iconPath
    }

    String getToken() {
        return token
    }

    void setToken(String token) {
        this.token = token
    }


    @Override
    public String toString() {
        return "FirExtension{" +
                "appName='" + appName + '\'' +
                ", iconPath='" + iconPath + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}