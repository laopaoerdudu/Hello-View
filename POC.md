### 桥接

WebView 的桥接，也就是 App 可以调用 Web 中的 js 方法，js 也可以调用 App 中的方法

新建 test_default_bridge.html 文件

```
<!DOCTYPE html>
<html>
<head>
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <script src='../js/bridge.js'></script>
    <style type="text/css">
   .bn {
      padding: 8px 20px;
      width: 100%;
      height: auto;
      margin: 0 auto;
      text-align: center;
      margin-top: 20px;
   }
    </style>
</head>
<body>
<div class="detail-content" id="app-vote">
    <div style="display: flex; flex-direction: column;">
        <button class="bn" onclick="showToast()">调用原生 App 展示 Toast</button>
    </div>
</div>
</body>
<script type='text/javascript'>
function showToast() {
   // bridge 要和 BaseWebView 中 addJavascriptInterface 第二个参数对应
   window.bridge.showToast('hello world')
}
</script>
```

### App 调用 Web 中的 js 方法

```
// 写正确 方法名 和 参数 即可
mWebView.evaluateJavascript("javascript:showToast()") {}
```

### js 调用 App 中的方法

```
open class BaseWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : WebView(context, attrs), LifecycleEventObserver {

    init {
        // 省略其他代码...
        // 添加桥接
        addJavascriptInterface(this, "bridge")
    }
    
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                Log.d(TAG, "onStateChanged -> ON_RESUME")
                onResume()
            }
            Lifecycle.Event.ON_STOP -> {
                Log.d(TAG, "onStateChanged -> ON_STOP")
                onPause()
            }
            Lifecycle.Event.ON_DESTROY -> {
                Log.d(TAG, "onStateChanged -> ON_DESTROY")
                source.lifecycle.removeObserver(this)
                onDestroy()
            }
        }
    }
    
    // 添加注解 表示 js 可以调用该方法
    @JavascriptInterface
    fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
```