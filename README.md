# OneKeyPerm
一键申请Android权限


## OneKeyPerm接入说明

OneKeyPerm不依赖任何业务Activity，支持多进程，只需一句静态代码就可以了

例如

```java
OneKeyPerm.request(Manifest.permission.CAMERA, "需要使用相机权限"
                , new OneKeyPerm.OnPermResultListener() {
                    @Override
                    public void onPermResult(String perm, boolean isGrant) {
                        Toast.makeText(MainActivity.this, "请求相机权限 " + isGrant, Toast.LENGTH_SHORT).show();
                    }
                });
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

之后就可以一句话申请权限了




