# OneKeyPerm
一键申请Android权限


### OneKeyPerm接入说明


 
OneKeyPerm不依赖任何业务Activity，支持多进程，只需一句静态代码就可以了

例如

```java
申请权限被拒绝后 不会 自动开启设置页面让用户手动开启权限

OneKeyPerm.request(application, Manifest.permission.CAMERA, "您需要允许相机权限，否则无法使用扫码功能", new OneKeyPerm.OnPermResultListener() {
                    @Override
                    public void onPermResult(String perm, boolean isGrant) {
                        Toast.makeText(MainActivity.this, "请求相机权限 " + isGrant, Toast.LENGTH_SHORT).show();
                    }
                });
```

或者

```java
申请权限被拒绝后 会 自动开启设置页面让用户手动开启权限

OneKeyPerm.request(application, Manifest.permission.CAMERA, "您需要允许相机权限，否则无法使用扫码功能", new OneKeyPerm.OnPermResultListener() {
                    @Override
                    public void onPermResult(String perm, boolean isGrant) {
                        Toast.makeText(MainActivity.this, "请求相机权限 " + isGrant, Toast.LENGTH_SHORT).show();
                    }
                },true);
```



### 原理分析

* 每次通过context启动透明Activity`（PermissionActivity）`请求权限

* 当权限被拒绝后启动另一个透明Activity `(WatchAuthorizationActivity)`,在`WatchAuthorizationActivity`中再次启动应用详情设置Activity，然后在`WatchAuthorizationActivity`的`onActivityResult`方法中再次检查是否已经手动授权，并通过Binder（解决多进程问题）通知调用者


备注：收回授权后Android会重启App





