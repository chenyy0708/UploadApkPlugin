package com.minic.uploadplugin

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory

class UploadApkPluginExtension {
    FirExtension firExtension
    PgyerExtension pgyerExtension

    public UploadApkPluginExtension(ObjectFactory objectFactory) {
        firExtension = objectFactory.newInstance(FirExtension.class)
        pgyerExtension = objectFactory.newInstance(PgyerExtension.class)
    }

    public void fir(Action<FirExtension> action) {
        action.execute(firExtension)
    }

    public void pgyer(Action<FirExtension> action) {
        action.execute(pgyerExtension)
    }
}