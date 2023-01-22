### style

```
<resources>
    <style name="myStyle">
        <item name="attr_str">str in myStyle</item>
        <item name="attr_bool">true</item>
    </style>
</resources>
```

将这些属性提取出来作为一个style项，在引用的时候引用style即可，不用到处重复定义属性。

```
<com.rko.MyAttrView
    style="@style/myStyle"
    android:layout_width="100px"
    android:layout_height="100px">
</com.fish.myapplication.attr.MyAttrView>
```

### theme

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="myTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="attr_str">str in myTheme</item>
        <item name="attr_bool">true</item>
    </style>
</resources>
```

style 为了 View 之间复用属性集，那么 theme 是为了 Activity/Application 复用属性集。因此，我们将 theme 配置给 Activity或者Application。

总结来说三者关系：

1，style 是定义属性的集合，使用 style 标签，作用于 View。

2，theme 是定义属性的集合，使用 style 标签，作用于 Application/Activity。

3，declare-styleable 是声明属性的集合，使用 declare-styleable 标签。

### 自定义属性优先级自高到低

1、在布局文件里定义属性。

2、在 style 里定义属性。

3、在 theme 里定义属性。

4、默认的属性。

5、默认的 style。






























