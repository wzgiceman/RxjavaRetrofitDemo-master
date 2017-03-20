# 问题答疑

看之前先来个start支持下

### 项目地址

* Gson方案：https://github.com/wzgiceman/RxjavaRetrofitDemo-master
* String方案：https://github.com/wzgiceman/RxjavaRetrofitDemo-string-master
* 详细封装过程：http://blog.csdn.net/column/details/13297.html

Gson方案引入最新的`com.squareup.retrofit2:converter-gson`已经解决返回null数据的问题
但是个人还是推荐使用String方案，灵活


### 缺少了DaoMaster，DaoSession，CookieResulteDao等类

数据库处理类是greendao自动生成的，可以clean以后build如果还是失败，检查更目录下build.gradle是否添加greendao引用




### 为何把Api单独封装

将api单独封装一个类是为了有利于代码的高聚，让api逻辑和主逻辑区分，减少逻辑嵌套的阅读理解成本。
设想：如果100个接口，一种是写入到逻辑代码中（确实能相对减少部分代码量，但是你确定你自己能看懂？你的后任看了不会骂娘），还是写100个api类，分别写上注释更让人容易理解呢


### 统一的处理如何修改

比如tokean过期这样的处理，可以将失败的统一处理逻辑放入到HttpManager中

* 可以高效的统一封装，HttpManager中有网络请求的所有对象
* 减少耦合度，方便修改







