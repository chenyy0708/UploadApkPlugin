### 一键上传Debug包到Fir.im、蒲公英等测试平台

[![](https://jitpack.io/v/chenyy0708/UploadApkPlugin.svg)](https://jitpack.io/#chenyy0708/UploadApkPlugin)

适用于公司频繁的打包测试，由于公司安装了Jenkins的机器`Android`和`iOS`共用，`iOS`在打包的时候会占用电脑资源，如果两个一起打包会两个都极其的慢，为了解决这个问题，最近研究了下
`Gradle`插件上传`Debug`包。

#### 引入项目

##### 1. 配置

1. 在项目根目录的`build.gradle`配置如下：

```
buildscript {

  ....
  repositories {

        ....
        maven { url 'https://jitpack.io' }
        
        dependencies {
         classpath 'com.github.chenyy0708:UploadApkPlugin:最新版本'
        }
    }


}
```

注意，是项目根目录、根目录、根目录!!!

2. 在`app`的`build.gradle`配置如下

```
// 引入插件到app
apply plugin: 'upload.apk'

// 配置上传信息
uploadApkInfo {

    // 测试平台显示的应用名
    appName = "TestApp"
    // 测试平台显示的图片icon，只需要配置drawable/mipmap路径即可
    appIconPath = "mipmap-xxhdpi/ic_launcher.png"
    // Fir.im平台apitoken
    apiTokenFir = "368967b71745181a78ea8cb01ab237c5"
    // 蒲公英平台配置信息
    uKeyPgyer = "555a5e675f08277800b9a72ca447518f"
    apiKeyPgyer = "6f3611f328fea966c664d482be682040"
    // 蒲公英上传的类型
    installTypePgyer = "2"
    // 蒲公英平台安装密码，蒲公英更新之后都需要，否则无法上传app
    passWordPgyer = "123456"
    
}
```

##### 2. 使用

> 只需要双击`AS`的`gradle`脚本


**上传蒲公英**
`AS右侧边栏->app->Task->other->assembleWithPgyer`

**上传Fir .im**
`AS右侧边栏->app->Task->other->assembleWithFirim`


> 示例图

![WX20191219-115708.png](http://user-gold-cdn.xitu.io/2019/12/19/16f1c4dedaa1986e?w=500&h=766&f=png&s=88258)

等待apk上传成功即可，下载地址会在控制台打印：

```
> Task :app:transformResourcesWithMergeJavaResForDebug UP-TO-DATE
> Task :app:packageDebug UP-TO-DATE
> Task :app:assembleDebug UP-TO-DATE

> Task :app:assembleWithPgyer
开始上传蒲公英...



上传蒲公英apk文件返回结果:{"code":0,"message":"","data":{"appKey":"xxxx","userKey":"xxxx","appType":"2","appIsLastest":"1","appFileSize":"7950121","appName":"Gank","appVersion":"1.0.0","appVersionNo":"1","appBuildVersion":"1","appIdentifier":"com.minic.kt","appIcon":"xxxxxx","appDescription":"","appUpdateDescription":"","appScreenshots":"","appShortcutUrl":"O2fx","appCreated":"2019-12-20 22:08:11","appUpdated":"2019-12-20 22:08:11","appQRCodeURL":"https:\/\/www.pgyer.com\/app\/qrcodeHistory\/062c5xxxxxxx7158009c12ec4cb"}}
```




