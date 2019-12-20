package com.minic.uploadplugin

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

import java.util.concurrent.TimeUnit

class UploadApk implements Plugin<Project> {

    UploadApkPluginExtension extension

    @Override
    void apply(Project project) {
        extension = project.extensions.create('uploadApkInfo', UploadApkPluginExtension)
        if (project.android.hasProperty("applicationVariants")) {
            project.android.applicationVariants.all { variant ->
                String variantName = variant.name.capitalize()
                if (variantName.equals("Debug")) {
                    Task uploadFir = createUploadFirTask(project)
                    Task uploadPgyer = createUploadPgyerTask(project)
                    // 在assembleDebug执行后执行
                    uploadFir.dependsOn project.tasks["assembleDebug"]
                    uploadPgyer.dependsOn project.tasks["assembleDebug"]
                }
            }

        }
    }

    private Task createUploadFirTask(Project project) {
        println("upload:create uploadFirTak")
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
            // 获取上传凭证
            getUploadCertificate(appPackage, apiTokenFir)

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

    private void getUploadCertificate(String bundle_id, String api_token) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build()

        FormBody.Builder build = new FormBody.Builder()
        build.add("bundle_id", bundle_id)
        build.add("api_token", api_token)
        build.add("type", "android")
        Request request = new Request.Builder().url("http://api.fir.im/apps").post(build.build()).build()
        Response response = client.newCall(request).execute()
        String is = response.body().string()
        println("getAppinfo result:" + is)
    }
}
