#### app 热启动和冷启动的区别

当 app 已经被打开， 按下返回键或 Home 键，再重新打开该 app 时， 这个方式叫做热启动。

>热启动会从已有的进程来启动，所以就不会走 Application 这步了，而是直接走 MainActivity(包括一系列的测量、布局、绘制)，
> 所以热启动的过程只需要创建和初始化一个 MainActivity 就行了，而不必创建和初始化 Application。

后台不存在该应用进程，系统会重新创建一个新的进程分配给该应用， 这个启动方式就叫做冷启动。
所以会先创建和初始化 Application 类，再创建和初始化 MainActivity类(包括一系列的测量、布局、绘制)，最后显示在界面上。

实践中的 App 跳转或路由行为可远不止这两种， 那么，这么多入口的路由行为如果不统一起来，对于后续的维护工作会劣化。
所以这块需要把 `SchemeHelper` 中的协议解析部分统一封装为全局可用的路由分发器 `RootUrlDispatcher` ，实现收口。

• App 原生跳转。

• App 网页容器跳转 App 原生页。

• Scheme 协议唤醒 App 的跳转（如前所述，分为冷启动和热启动）。

• Push 消息唤醒 App 跳转（分为端外推送和端内推送）。

adb 测试： 如果开发阶段自测时需要依赖 Web 端给我们提供一个网页来拉起 App，测试效率就太低了。我们可以使用 adb 命令来自测：

>命令模板：
adb shell am start
-W -a android.intent.action.VIEW
-d <URI-定义的URI> <PACKAGE-需要测试的应用包名>

示例：

```
adb shell am start
    -W -a android.intent.action.VIEW
    -d "xiaopeng://www.myapp.com/goods/?goodsId=123456" com.xiaopeng.app
```

#### 自定义 Scheme 协议设计

一般推荐采用这种格式的

```
URI：scheme://host/path?query
```

例如，链接 `xiaopeng://www.myapp.com/goods?goodsId=123456&size=1` 打开商品详情页，并且选择 size=1 的规格。

scheme -> xiaopeng:// -> 业务独有的领域，一个 App 可以支持多个 Scheme

host -> www.myapp.com -> 某一个子域名

path -> /goods -> 页面路径，可以多级别

query -> ?goodsId=123456&size=1 -> 页面参数，可以多参数

这里需要注意下几个细节：

• 登录引导：

>我们定义了 `needLogin` 这个参数呢，因为实践中发现用户的账单详情页这一类落地页是一定要求用户登录的。
> 所以我们在拉起落地页之前增加了一个登录引导，在登录成功后再进入落地页。
> 例如，链接 `xiaopeng://www.myapp.com/goods?goodsId=123456&size=1?needLogin=1`
> 表示打开 App，先要求用户登录后再打开商品详情页，并且选择 size=1 的规格；

• H5 跳转：

>有一些活动页是需要通过网页容器来承载的，因此我们希望打开 App 后唤起 `MyWebViewActivity` 网页容器来显示。
>对于这样的场景我们可以直接使用 `http` 或 `https` 作为 `Scheme`，App 将这类链接直接转交给 `MyWebViewActivity` 去呈现；

• 数据加密：

>为了提高安全性，URI 中的 `path?query` 的部分可以使用加密算法，`scheme://host` 的部分需要用于匹配，并且不带有风险数据，可以不加密。

当用户想在 APP 上找到某个感兴趣的页面时，深度链接（Deeplink）是一个可以从任何地方将用户带到应用内容页的简单方式。你用起来了吗？

#### 浏览器为什么能唤起App的Activity？

html 标签有一个属性 `href`，比如：`<a href="...">`。

我们常见的一种用法：`<a href="``https://www.baidu.com``">`。也就是点击之后跳转到百度。

当然这里和 android 交互的流程基本一致：用隐式调用的方式，声明需要启动的 `Activity`；然后 `<a href="">传入对应的协议（scheme`）即可。比如：

```
https://www.baidu.com?key=hello

[scheme:][host][path][?query] 
```

前端页面：

```
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<a href="mdove://wwe"> 启动 WWEActivity </a>
</body>
```

android声明：

```
<activity
    android:name=".WWEActivity"
    android:screenOrientation="portrait">
    <intent-filter>
        <data
            android:host="wwe"
            android:scheme="mdove" />
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.BROWSABLE" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```






















