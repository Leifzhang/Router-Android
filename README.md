# AndroidRouter
It's an Android Route Library. You can just add some Annotation to add you router path.
## Usage
Add this line to your `build.gradle` file under your module directory.

```
dependencies {
    compile 'com.github.leifzhang:routerLib:0.4.1'
    annotationProcessor "com.github.leifzhang:compiler:0.4.1"
}
```
# First Step
You should create you activity which need add router.Then just add like this.
```java 
@BindRouter(urls = {"https://wwww.github.com"})
public class TestActivity extends Activity {

}
```
If you need just urls and add some params.You can  just  add like this.
```java
 @BindRouter(urls = { "https://github.com/leifzhang"}, weight=10)
 public class TestActivity extends Activity {

 }
```
Also maybe some callback not an activity. You can do like this.
```java
@BindRouter(urls = {"https://wwww.baidu.com"}, isRunnable = true)
public class SimpleCallBack implements RouterCallback {
    @Override
    public void run(RouterContext context) {
        Toast.makeText(context.getContext(), "testing", Toast.LENGTH_SHORT).show();
    }
}
```
# Second Step
Each module need a Annotation just call `BindModule`
```java
@BindModule("app")
public class Module {

}
```

# Last Step
You just should easy use you code just like this.
```java
    Router.sharedRouter().open("https://github.com/leifzhang", this);
```

