# OneKeyPerm
一键申请Android权限


### OneKeyPerm接入说明

OneKeyPerm不依赖任何业务Activity，支持多进程，只需一句静态代码就可以了

例如

```java
申请权限被拒绝后 不会 自动开启设置页面让用户手动开启权限

OneKeyPerm.request(Manifest.permission.CAMERA, "需要使用相机权限"
                , new OneKeyPerm.OnPermResultListener() {
                    @Override
                    public void onPermResult(String perm, boolean isGrant) {
                        Toast.makeText(MainActivity.this, "请求相机权限 " + isGrant, Toast.LENGTH_SHORT).show();
                    }
                });
```

或者

```java
申请权限被拒绝后 会 自动开启设置页面让用户手动开启权限

OneKeyPerm.request(Manifest.permission.CAMERA, "需要使用相机权限"
                , new OneKeyPerm.OnPermResultListener() {
                    @Override
                    public void onPermResult(String perm, boolean isGrant) {
                        Toast.makeText(MainActivity.this, "请求相机权限 " + isGrant, Toast.LENGTH_SHORT).show();
                    }
                },true);
```

### 接入

* 在清单中添加自定义权限，例如:

```html

<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.wanjian.permission.demo">

    <!--permission中com.wanjian.permission.demo替换成你的包名-->
    <permission
        android:name="com.wanjian.permission.demo.permission.ONE_KEY_PERM"
        android:protectionLevel="signature" />

    <!--uses-permission中com.wanjian.permission.demo替换成你的包名-->
    <uses-permission android:name="com.wanjian.permission.demo.permission.ONE_KEY_PERM" />

  
</manifest>

```

其中：
permission节点中name要替换成你的app的`包名.permission.ONE_KEY_PERM`，
uses-permission节点中的name也要替换成你的app的`包名.permission.ONE_KEY_PERM`

* 在Application中调用 ` OneKeyPerm.install(this);`初始化，例如

```java


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OneKeyPerm.install(this);
    }
}


```
* 如果manifest.xml没有配置`ONE_KEY_PERM`权限的话，运行app会报错，直接把错误堆栈信息中的权限配置复制到manifest.xml中也可以

例如

```xml

 FATAL EXCEPTION: main
 Process: com.wanjian.permission.demo, PID: 6282
 java.lang.RuntimeException: Unable to create application com.wanjian.permission.demo.App: java.lang.RuntimeException: define and use permission in your manifest ! 
 example:
  <permission
         android:name="com.wanjian.permission.demo.permission.ONE_KEY_PERM"
         android:protectionLevel="signature" />
  <uses-permission android:name="com.wanjian.permission.demo.permission.ONE_KEY_PERM" />


```

之后就可以一句话申请权限了


### 原理分析

* 每次通过context启动透明Activity`（PermissionActivity）`请求权限

* 当权限被拒绝后启动另一个透明Activity `(WatchAuthorizationActivity)`,在`WatchAuthorizationActivity`中再次启动应用详情设置Activity，然后在`WatchAuthorizationActivity`的`onActivityResult`方法中再次检查是否已经手动授权，并通过广播（解决多进程问题）通知调用者


备注：收回授权后Android会重启App





