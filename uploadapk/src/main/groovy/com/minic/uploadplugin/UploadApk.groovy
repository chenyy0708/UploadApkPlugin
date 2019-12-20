package com.minic.uploadplugin

import okhttp3.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.json.JSONObject

import java.util.concurrent.TimeUnit

class UploadApk implements Plugin<Project> {

    UploadApkPluginExtension extension

    @Override
    void apply(Project project) {
        extension = project.extensions.create('uploadApkInfo', UploadApkPluginExtension)
        if (project.android.hasProperty("applicationVariants")) {
            project.android.applicationVariants.all { variant ->
                String variantName = variant.name.capitalize()
                if (variantName == "Debug") {
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
                        // 获取上传凭证
                        JSONObject infoObj = getUploadCertificate(appPackage, apiTokenFir)
                        // 上传apk
                        uploadApkToFir(appName, appVersion, appBuild, apkPath, infoObj)
                        // 上传icon
                        uploadIconToFir(apkIconPath, infoObj)
                        // 获取成功连接
                        getUploadSuccessUrl(appPackage, apiTokenFir)
                    }
                    Task uploadPgyer = project.task("assembleWithPgyer").doLast {
                        println("开始上传蒲公英...")
                        String apkPath = project.android.applicationVariants.first().outputs.first().outputFile
                        String apiKeyPgyer = extension.apiKeyPgyer
                        String uKeyPgyer = extension.uKeyPgyer
                        String installTypePgyer = extension.installTypePgyer
                        String passWordPgyer = extension.passWordPgyer

                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(60, TimeUnit.SECONDS).build()
                        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(apkPath))

                        MultipartBody body = new MultipartBody.Builder()
                                .setType(MediaType.parse("multipart/form-data"))
                                .addFormDataPart("uKey", uKeyPgyer)
                                .addFormDataPart("_api_key", apiKeyPgyer)
                                .addFormDataPart("installType", installTypePgyer)
                                .addFormDataPart("password", passWordPgyer)
                                .addFormDataPart("file", "boohee.apk", fileBody)
                                .build()
                        Request request = new Request.Builder().url("https://upload.pgyer.com/apiv1/app/upload").post(body).build()
                        Response response = client.newCall(request).execute()
                        String json = response.body().string()
                        println("上传蒲公英apk文件返回结果:" + json)
                    }
                    // 在assembleDebug执行后执行
                    uploadFir.dependsOn project.tasks["assembleDebug"]
                    uploadPgyer.dependsOn project.tasks["assembleDebug"]
                }
            }
        }
    }

    private JSONObject getUploadCertificate(String bundle_id, String api_token) {
        println("获取上传凭证...")
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build()

        FormBody.Builder build = new FormBody.Builder()
        build.add("bundle_id", bundle_id)
        build.add("api_token", api_token)
        build.add("type", "android")
        Request request = new Request.Builder().url("http://api.fir.im/apps").post(build.build()).build()
        Response response = client.newCall(request).execute()
        if (response == null || response.body() == null) return null
        String is = response.body().string()
        println("获取凭证信息成功")
        JSONObject json = new JSONObject(is)
        return json
    }

    private void uploadApkToFir(String appName, String appVersion, String appBuild, String apkPath, JSONObject jsonObject) {
        println("上传apk中...")
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build()
        String key = jsonObject.getJSONObject("cert").getJSONObject("binary").getString("key")
        String token = jsonObject.getJSONObject("cert").getJSONObject("binary").getString("token")
        String upload_url = jsonObject.getJSONObject("cert").getJSONObject("binary").getString("upload_url")

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(apkPath))

        MultipartBody body = new MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data"))
                .addFormDataPart("key", key)
                .addFormDataPart("token", token)
                .addFormDataPart("x:name", appName)
                .addFormDataPart("x:version", appVersion)
                .addFormDataPart("x:build", appBuild)
                .addFormDataPart("file", "boohee.apk", fileBody)
                .build()

        Request request = new Request.Builder().url(upload_url).post(body).build()
        Response response = client.newCall(request).execute()
        String json = response.body().string()
        println("上传apk文件返回结果:" + json)
    }

    private void uploadIconToFir(String apkIconPath, JSONObject jsonObject) {
        println("上传Icon中...")
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build()
        String key = jsonObject.getJSONObject("cert").getJSONObject("icon").getString("key")
        String token = jsonObject.getJSONObject("cert").getJSONObject("icon").getString("token")
        String upload_url = jsonObject.getJSONObject("cert").getJSONObject("icon").getString("upload_url")

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(apkIconPath))

        MultipartBody body = new MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data"))
                .addFormDataPart("key", key)
                .addFormDataPart("token", token)
                .addFormDataPart("file", "icon.png", fileBody)
                .build()
        Request request = new Request.Builder().url(upload_url).post(body).build()
        Response response = client.newCall(request).execute()
        String json = response.body().string()
        println("上传Icon返回结果:" + json)
    }

    private void getUploadSuccessUrl(String appPackage, String apiToken) {
        String queryurl = "http://api.fir.im/apps/latest/" + appPackage + "?api_token=" + apiToken + "&type=android"

        println(queryurl)

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build()
        Request request = new Request.Builder().url(queryurl).get().build()
        Response response = client.newCall(request).execute()
        String is = response.body().string()
        println("下载链接:" + is)
    }
}
