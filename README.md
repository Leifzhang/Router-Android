# 祖传路由项目

简单的说功能大部分和阿里美团的路由都半斤八两吧，以前公司拿来做组件化拆分的，支持编译时注册以及增量编译等等，整体kt重构过一次。


支持参数跳转，以及startActivityForResult操作，并提供成功失败回掉监听等。


同时项目升级了kapt版本，已经支持kapt的增量编译了。

## 使用大法

1. 根目录 `build.gradle` 添加

```gradle
buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        classpath 'com.kronos.plugin:AutoRegister:0.5.4'
    }
}
```
2. 项目目录增加路由注册插件

```
apply plugin: 'router-register'

dependencies {
    compile 'com.github.leifzhang:routerLib:0.5.1'
    kapt "com.github.leifzhang:compiler:0.5.1"
}
```

3. 给`Activity`或`RouterCallback`添加注解

```java 
@BindRouter(urls = {"https://wwww.github.com"})
public class TestActivity extends Activity {

}
```

```java 
@BindRouter(urls = {"https://wwww.baidu.com"}, interceptors = {TestInterceptor.class})
public class SimpleCallBack implements RouterCallback {
    @Override
    public void run(RouterContext context) {
        Toast.makeText(context.getContext(), "testing", Toast.LENGTH_SHORT).show();
    }
}

```

4. 万一有高仿的路由出现，可以这样

```java
 @BindRouter(urls = { "https://github.com/leifzhang"}, weight=10)
 public class TestActivity extends Activity {

 }
```

5.  启动一个路由跳转

直接启动可以用这个:

```java
    Router.sharedRouter().open("https://github.com/leifzhang", this);
```
复杂的多参数传递可以用这个:

```kotlin
  val request = KRequest("https://www.baidu.com/test", onSuccess = {
                    Log.i("KRequest", "onSuccess")
                }, onFail = {
                    Log.i("KRequest", "onFail")
                }).apply {
                    activityResultCode = 12345
                }.start(this)
```

## 如果是Module的特别注意

```kotlin
kapt {
    arguments {
        arg("ROUTER_MODULE_NAME", project.getName())
    }
}
```


## 有事？

可以qq找我 454327998