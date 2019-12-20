package com.minic.uploadplugin

import okhttp3.OkHttpClient
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class UploadApk implements Plugin<Project> {

    UploadApkPluginExtension extension

    @Override
    void apply(Project project) {
        extension = project.extensions.create('uploadApkInfo', UploadApkPluginExtension)
        if (project.android.hasProperty("applicationVariants")) {
            Task uploadFir = createUploadFirTask(project)
            Task uploadPgyer = createUploadPgyerTask(project)
        }
    }

    private Task createUploadFirTask(Project project) {
        Task uploadFir = project.task("assembleWithFir").doLast {
            println("开始上传Fir")
            String appName = extension.appName
            String appPackage = project.android.defaultConfig.applicationId
            String appVersion = project.android.defaultConfig.versionName
            String appBuild = project.android.defaultConfig.versionCode
            String apkPath = project.android.applicationVariants.first().outputs.first().outputFile
            String apkIconPath = project.android.applicationVariants.first().outputs.first().outputFile.parent.split("build")[0] +
                    "src/main/res/" + extension.appIconPath
            String apiTokenFir = extension.apiTokenFir
            println("appName:" + appName)
            println("appPackage:" + appPackage)
            println("appVersion:" + appVersion)
            println("appBuild:" + appBuild)
            println("apkPath:" + apkPath)
            println("apkIconPath:" + apkIconPath)
            println("apiTokenFir:" + apiTokenFir)
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .build()
            println("Okhttp:" + okHttpClient.hashCode())
        }
        return uploadFir
    }

    private Task createUploadPgyerTask(Project project) {
        Task uploadPgyer = project.task("assembleWithPgyer").doLast {
            println("开始上传蒲公英")
            String apkPath = project.android.applicationVariants.first().outputs.first().outputFile
            String apiKeyPgyer = extension.apiKeyPgyer
            String uKeyPgyer = extension.uKeyPgyer
            String installTypePgyer = extension.installTypePgyer
            String passWordPgyer = extension.passWordPgyer
            println("apkPath:" + apkPath)
            println("apiKeyPgyer:" + apiKeyPgyer)
            println("uKeyPgyer:" + uKeyPgyer)
            println("installTypePgyer:" + installTypePgyer)
            println("passWordPgyer:" + passWordPgyer)
        }
        return uploadPgyer
    }
}
