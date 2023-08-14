## 插件化换肤整体思路

1，定义换肤功能接口，让需要换肤的 View 实现自己的换肤逻辑。

2，给 LayoutInflater 设置自定义的 Factory2，将 XML 中的 View 改为实现换肤接口的 View，并且将 View 记录下来。

3，制作皮肤包，这里让皮肤包和 App 本身资源名相同，值不同，这样换肤时，根据 View 设置的资源名去皮肤包中找同名资源。

4，换肤时，循环记录下来的换肤 View，调用其换肤方法即可。

Ref:

https://mp.weixin.qq.com/s/mg7zzt7vNhT99poMuMv8tQ

https://mp.weixin.qq.com/s/gywtmRcvw865SzChWtXWVg
