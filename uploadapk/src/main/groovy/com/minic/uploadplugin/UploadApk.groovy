package com.minic.uploadplugin

import okhttp3.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.json.JSONObject

import java.util.concurrent.TimeUnit

class UploadApk implements Plugin<Project> {

    private int connectTime = 2000

    UploadApkPluginExtension extension

    @Override
    void apply(Project project) {
        extension = project.extensions.create('uploadApkInfo', UploadApkPluginExtension)
        if (project.android.hasProperty("applicationVariants")) {
            project.android.applicationVariants.all { variant ->
                String variantName = variant.name.capitalize()
                if (variantName == "Debug") {
                    // Fir上传
                    Task uploadFir = project.task("assembleWithFir").doLast {
                        println("开始上传Fir")
                        String appName = extension.appName
                        String appPackage = project.android.defaultConfig.applicationId
                        String appVersion = project.android.defaultConfig.versionName
                        String appBuild = project.android.defaultConfig.versionCode
                        String apkPath = project.android.applicationVariants.first().outputs.first().outputFile
                        String apkIconPath = project.android.applicationVariants.first().outputs.first().outputFile.parent.split("build")[0] + extension.appIconPath
                        String apiTokenFir = extension.apiTokenFir
                        // 获取上传凭证
                        println("获取上传凭证...")
                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(connectTime, TimeUnit.SECONDS)
                                .readTimeout(connectTime, TimeUnit.SECONDS).build()
                        FormBody.Builder build = new FormBody.Builder()
                        build.add("bundle_id", appPackage)
                        build.add("api_token", apiTokenFir)
                        build.add("type", "android")
                        Request request = new Request.Builder().url("http://api.fir.im/apps").post(build.build()).build()
                        Response response = client.newCall(request).execute()
                        String is = response.body().string()
                        println("获取凭证信息成功")
                        JSONObject jsonObject = new JSONObject(is)
                        // 上传apk
                        println("上传apk中...")
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
                                .addFormDataPart("file", "appDebug.apk", fileBody)
                                .build()
                        Request requestApk = new Request.Builder().url(upload_url).post(body).build()
                        Response responseApk = client.newCall(requestApk).execute()
                        String jsonApk = responseApk.body().string()
                        println("上传apk文件返回结果:" + jsonApk)
                        // 上传icon
                        println("上传Icon中...")
                        String keyIcon = jsonObject.getJSONObject("cert").getJSONObject("icon").getString("key")
                        String tokenIcon = jsonObject.getJSONObject("cert").getJSONObject("icon").getString("token")
                        String upload_urlIcon = jsonObject.getJSONObject("cert").getJSONObject("icon").getString("upload_url")
                        RequestBody fileBodyIcon = RequestBody.create(MediaType.parse("application/octet-stream"), new File(apkIconPath))
                        MultipartBody bodyIcon = new MultipartBody.Builder()
                                .setType(MediaType.parse("multipart/form-data"))
                                .addFormDataPart("key", keyIcon)
                                .addFormDataPart("token", tokenIcon)
                                .addFormDataPart("file", "icon.png", fileBodyIcon)
                                .build()
                        Request requestIcon = new Request.Builder().url(upload_urlIcon).post(bodyIcon).build()
                        Response responseIcon = client.newCall(requestIcon).execute()
                        String jsonIcon = responseIcon.body().string()
                        println("上传Icon返回结果:" + jsonIcon)
                        // 获取成功连接
                        String queryurl = "http://api.fir.im/apps/latest/" + appPackage + "?api_token=" + apiTokenFir + "&type=android"
                        Request requestUrl = new Request.Builder().url(queryurl).get().build()
                        Response responseUrl = client.newCall(requestUrl).execute()
                        String isUrl = responseUrl.body().string()
                        println("下载链接:" + isUrl)
                    }
                    // 蒲公英上传
                    Task uploadPgyer = project.task("assembleWithPgyer").doLast {
                        println("开始上传蒲公英...")
                        String apkPath = project.android.applicationVariants.first().outputs.first().outputFile
                        String apiKeyPgyer = extension.apiKeyPgyer
                        String uKeyPgyer = extension.uKeyPgyer
                        String installTypePgyer = extension.installTypePgyer
                        String passWordPgyer = extension.passWordPgyer

                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(connectTime, TimeUnit.SECONDS)
                                .readTimeout(connectTime, TimeUnit.SECONDS).build()
                        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(apkPath))

                        MultipartBody body = new MultipartBody.Builder()
                                .setType(MediaType.parse("multipart/form-data"))
                                .addFormDataPart("uKey", uKeyPgyer)
                                .addFormDataPart("_api_key", apiKeyPgyer)
                                .addFormDataPart("installType", installTypePgyer)
                                .addFormDataPart("password", passWordPgyer)
                                .addFormDataPart("file", "appDebug.apk", fileBody)
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
}
