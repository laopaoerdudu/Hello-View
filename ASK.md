### Resources在什么时候被解析并加载的？

1，在程序执行 `ActivityThread.main()` 方法的时候，我们会在 `ActivityThread.attach()` 中创建 `Context`，创建 `Application`，
并且执行 `Application #onCreate()`

2，然后会执行 `LoadedApk.getResources()` 去解析获取 Resources()
>`LoadedApk.java` 从类名我们就知道这个类是用来对 apk 信息解析的。

3，最终解析 `Resources` 的任务交给了 `ResourcesManager #createResources()`。

>Resources 中包含 ResourcesImpl，而 ResourcesImpl 中又包含着 AssetManager，真正做事情的是 AssetManager。

获取到当前程序在手机内存中的路径：

```
getApplicationContext().getPackageResourcePath()
```

大多数 App 的换肤功能是不需要重启页面的，那么就有两个问题需要搞明白：

1，如何通知 View 进行换肤操作？

2，换肤是如何加载皮肤包中的资源的？

```
public final View tryCreateView(@Nullable View parent, @NonNull String name,
    // ...
    View view;
    if (mFactory2 != null) { // 优先通过 mFactory2 创建 View
        view = mFactory2.onCreateView(parent, name, context, attrs);
    } else if (mFactory != null) {  // 第二选择 mFactory 创建 View
        view = mFactory.onCreateView(name, context, attrs);
    } else { // 都创建失败返回 null
        view = null;
    }
    // ...
    return view;
}
```

两个 Factory 均是 LayoutInflate 的内部接口，且 Activity 也实现了 Factory2 接口。

```
# LayoutInflate.java

public interface Factory {
    @Nullable
    View onCreateView(@NonNull String name, @NonNull Context context,
            @NonNull AttributeSet attrs);
}

public interface Factory2 extends Factory {
    @Nullable
    View onCreateView(@Nullable View parent, @NonNull String name,
            @NonNull Context context, @NonNull AttributeSet attrs);
}
```

### Factory2 是什么时候设置的？

```
# AppCompatActivity.java

@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    final AppCompatDelegate delegate = getDelegate();
    delegate.installViewFactory(); // 注意这行代码
    delegate.onCreate(savedInstanceState);
    super.onCreate(savedInstanceState);
}
```

```
# AppCompatDelegate.java

public void installViewFactory() {
    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
    if (layoutInflater.getFactory() == null) {
        // 看到这里的 this 就说明 AppCompatDelegate 实现了 Factory2 接口
        LayoutInflaterCompat.setFactory2(layoutInflater, this);
    } else {
        if (!(layoutInflater.getFactory2() instanceof AppCompatDelegateImpl)) {
            Log.i(TAG, "The Activity's LayoutInflater already has a Factory installed"
                    + " so we can not install AppCompat's");
        }
    }
}
```

Ref:

https://juejin.cn/post/7153807668988084237



























