# 问题答疑

看之前先来个start支持下,动动手指，其实我们能做的还有很多！

### 项目地址

* Gson方案：https://github.com/wzgiceman/RxjavaRetrofitDemo-master
* String方案：https://github.com/wzgiceman/RxjavaRetrofitDemo-string-master
* 详细封装过程：http://blog.csdn.net/column/details/13297.html


### 混淆

混淆规则：http://www.jianshu.com/p/c02049ed035d

### Gson方案null数据解析错误

Gson方案引入最新的`com.squareup.retrofit2:converter-gson`已经解决返回null数据的问题
但是个人还是推荐使用String方案，灵活


### 缺少了DaoMaster，DaoSession，CookieResulteDao等类

数据库处理类是greendao自动生成的，可以clean以后build如果还是失败，检查更目录下build.gradle是否添加greendao引用


### 为何把Api单独封装

将api单独封装一个类是为了有利于代码的高聚，让api逻辑和主逻辑区分，减少逻辑嵌套的阅读理解成本。
设想：如果100个接口，一种是写入到逻辑代码中（确实能相对减少部分代码量，但是你确定你自己能看懂？你的后任看了不会骂娘），还是写100个api类，分别写上注释更让人容易理解呢

虽然如此，我也给出了解决方案，用法具体看源码：[RxRetrofit-String](https://github.com/wzgiceman/RxjavaRetrofitDemo-string-master)

### 如何统一处理请求头（例如tokean过期之类的需求）

比如tokean过期这样的处理，可以将失败的统一处理逻辑放入到RxJava的flatMap中处理

* 可以进群，群文件分享中有此功能的解决demo，可以自行查看；不懂可以问群里的兄弟

* 自行下载研究github：https://github.com/wzgiceman/RxRetrofit-tokean


### 为何不用Okhttp或者Retrofit自带的缓存

自带缓存之适用于get请求，并且需要服务器配合返回time标识缓存时间，基本不适用实际开发中（服务器开发人员基本不鸟你这个）


### Retrofit基础的使用方法
Retrofit基础使用google其实有很多，无非就是一些注解的使用，提供学习博客自行研究下

>http://blog.csdn.net/qq_24889075/article/details/52181133


### Fragment中如何使用HttpManager

可调用(RxAppCompatActivity)getContext()得到宿主RxAppCompatActivity

```java
 new HttpManager(this,(RxAppCompatActivity)getContext());
```

### 如何在Service中调用
service里面一般都是无生命周期的循环调用,可以copy一个HttpManager出来，去掉RxAppCompatActivity参数就可以了,以及注释掉Ober对象中的

```java
 xxx.compose(appCompatActivity.get().bindUntilEvent(ActivityEvent.DESTROY))
```