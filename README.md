# 祖传路由项目

[![State-of-the-art Shitcode](https://img.shields.io/static/v1?label=State-of-the-art&message=Shitcode&color=7B5804)](https://github.com/trekhleb/state-of-the-art-shitcode)



简单的说功能大部分和阿里美团的路由都半斤八两吧，以前公司拿来做组件化拆分的，支持编译时注册以及增量编译等等，整体kt重构过一次。


支持参数跳转，以及startActivityForResult操作，并提供成功失败回掉监听等。


同时项目升级了kapt版本，已经支持kapt的增量编译了。


新增了ksp支持，速度可以比kapt更快，理论上优化25%以上的注解解释器速度，同时ksp由于已经支持增编以及编译缓存，所以性能更好更优异。



## wmrouter 增量编译

如果使用wmrouter的各位，可以直接用我的插件替换工程内的路由初始化，应该能解决项目编译的问题。基本测试都通过了。

~~~
buildscript {
    dependencies {
        classpath 'com.kronos.plugin:AutoRegister:0.5.5'
          }
}
~~~

~~~
apply plugin: 'router-register'
AutoRegister {
    REGISTER_PACKAGE_NAME = "com.sankuai.waimai.router.generated.service"
    REGISTER_CLASS_NAME = "com.sankuai.waimai.router.generated.ServiceLoaderInit"
    REGISTER_FUNCTION_NAME = "init"
    REGISTER_CLASS_FUNCTION_NAME = "init"
}
~~~

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

    万一你不想用plugin了，我给你准备了一个慢一点的还在方式，以前偷的arouter的类反射机制的。

```gradle
dependencies {
    // 如果你不要用transform
    implementation project(':EmptyLoader')
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
DSL 版本
```kotlin
            request("https://www.baidu.com/test") {
                activityResultCode = 12345
                success {

                }
                fail {

                }
                bundle {
                    putString("1234", "1234")
                }
            }.start(this)

```
通过dsl的形式也可以使用路由

协程版本
```kotlin
    GlobalScope.launch {
                  val result = request("https://www.baidu.com/test") {
                      activityResultCode = 12345
                      bundle {
                          putString("1234", "1234")
                      }
                  }.dispatcher(this@MainActivity)
                  delay(1000)
                  withContext(Dispatchers.Main) {
                      Toast.makeText(
                          this@MainActivity,
                          if (result) "成功了" else "失败了",
                          Toast.LENGTH_SHORT
                      ).show()
                  }
              }
```
可以通过协程的形式，挂起恢复的获取到返回值。

## 如果是Module的特别注意

```kotlin
kapt {
    arguments {
        arg("ROUTER_MODULE_NAME", project.getName())
    }
}
```

## 使用ksp

这次新增的ksp可以完美和kapt进行支持，ksp compiler 暂时只支持全量kotlin

```kotlin
plugins {
    // 这个 id 就是在 versionPlugin 文件夹下 build.gradle.kts.kts 文件内定义的id
    id("com.android.library")
    id("com.google.devtools.ksp") version "1.4.30-1.0.0-alpha04"
}

dependencies {
    // 使用ksp进行注解生成
    ksp(project(":kspCompiler"))
}

// 子模块可以加上这个
ksp {
    arg("ROUTER_MODULE_NAME", project.name)
}
```
