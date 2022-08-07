
整个 ViewGroup 的事件分发流程我们来简单梳理一下：

1. Android事件分发是先传递到 ViewGroup，再由 ViewGroup 传递到 View 的。

2. 在 ViewGroup 中可以通过 onInterceptTouchEvent 方法对事件传递进行拦截，onInterceptTouchEvent方法返回true代表不允许事件继续向子View传递， 
返回false代表不对事件进行拦截，默认返回false。

5. 子View中如果将传递的事件消费掉，ViewGroup中将无法接收到任何事件。


