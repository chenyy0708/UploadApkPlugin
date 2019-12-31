package com.minic.uploadplugin

import org.gradle.api.Action
import org.gradle.internal.reflect.Instantiator;

class UploadApkPluginExtension {
    FirExtension firExtension
    PgyerExtension pgyerExtension

    public UploadApkPluginExtension(Instantiator instantiator) {
        firExtension = instantiator.newInstance(FirExtension.class)
        pgyerExtension = instantiator.newInstance(PgyerExtension.class)
    }

    FirExtension getFirExtension() {
        return firExtension
    }

    void setFirExtension(FirExtension firExtension) {
        this.firExtension = firExtension
    }

    PgyerExtension getPgyerExtension() {
        return pgyerExtension
    }

    void setPgyerExtension(PgyerExtension pgyerExtension) {
        this.pgyerExtension = pgyerExtension
    }

    public void fir(Action<FirExtension> action) {
        action.execute(firExtension)
    }

    public void pgyer(Action<PgyerExtension> action) {
        action.execute(pgyerExtension)
    }

}