package com.minic.uploadplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class UploadApk implements Plugin<Project> {

    @Override
    void apply(Project project) {

        def extension = project.extensions.create('uploadApkInfo', UploadApkPluginExtension)
        project.task("assembleWithFirim").doLast {
            println("开始上传Fir")
            if (project.android.hasProperty("applicationVariants")) {
                String appName = extension.appName
                String appPackage = project.android.defaultConfig.applicationId
                String appVersion = project.android.defaultConfig.versionName
                String appBuild = project.android.defaultConfig.versionCode
                String apkPath = project.android.applicationVariants.first().outputs.first().outputFile
                String apkIconPath = project.android.applicationVariants.first().outputs.first().outputFile.parent.split("build")[0] +
                        "src/main/res/" + extension.appIconPath
                String apiTokenFir = extension.apiTokenFir
                String apiKeyPgyer = extension.apiKeyPgyer
                String uKeyPgyer = extension.uKeyPgyer
                String installTypePgyer = extension.installTypePgyer
                String passWordPgyer = extension.passWordPgyer
                println("appName:" + appName)
                println("appPackage:" + appPackage)
                println("appVersion:" + appVersion)
                println("appBuild:" + appBuild)
                println("apkPath:" + apkPath)
                println("apkIconPath:" + apkIconPath)
                println("apiTokenFir:" + apiTokenFir)
                println("apiKeyPgyer:" + apiKeyPgyer)
                println("uKeyPgyer:" + uKeyPgyer)
                println("installTypePgyer:" + installTypePgyer)
                println("passWordPgyer:" + passWordPgyer)
            }
        }



        project.task("assembleWithPgyer").doLast {
            println("开始上传蒲公英")
            if (project.android.hasProperty("applicationVariants")) {
                String appName = extension.appName
                String appPackage = project.android.defaultConfig.applicationId
                String appVersion = project.android.defaultConfig.versionName
                String appBuild = project.android.defaultConfig.versionCode
                String apkPath = project.android.applicationVariants.first().outputs.first().outputFile
                String apkIconPath = project.android.applicationVariants.first().outputs.first().outputFile.parent.split("build")[0] +
                        "src/main/res/" + extension.appIconPath
                String apiTokenFir = extension.apiTokenFir
                String apiKeyPgyer = extension.apiKeyPgyer
                String uKeyPgyer = extension.uKeyPgyer
                String installTypePgyer = extension.installTypePgyer
                String passWordPgyer = extension.passWordPgyer
                println("appName:" + appName)
                println("appPackage:" + appPackage)
                println("appVersion:" + appVersion)
                println("appBuild:" + appBuild)
                println("apkPath:" + apkPath)
                println("apkIconPath:" + apkIconPath)
                println("apiTokenFir:" + apiTokenFir)
                println("apiKeyPgyer:" + apiKeyPgyer)
                println("uKeyPgyer:" + uKeyPgyer)
                println("installTypePgyer:" + installTypePgyer)
                println("passWordPgyer:" + passWordPgyer)
            }
        }
    }
}
