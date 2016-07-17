# AndroidRouter
###It's an Android Route Library.
# How to use
 First you should add you route format to Router
```java Router.sharedRouter().map("https://github.com/leifzhang", TestActivity.class);
 Router.sharedRouter().map("https://wwww.baidu.com/:string", TestActivity.class);```
You just should easy use you code just like this.
```java Router.sharedRouter().open("https://github.com/leifzhang", this);```
