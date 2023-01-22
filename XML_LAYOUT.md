## tools:showIn

这个属性一般是配合 `<include>` 标签使用的。当我们把子 layout 抽出去之后，它的布局是相对独立的效果。
在子 layout 的根布局使用 `tools:showIn` 指定父 layout，就可以实时预览在父 layout 中的效果了。

## ViewStub

当我们需要根据条件判断来显示哪个 view 的时候，当我们明确知道需要显示哪个 view 的时候，通过 `ViewStub` 把实际视图 inflate 进来，这样就避免了资源浪费。

```
<ViewStub
    android:id="@+id/stub_import"
    android:inflatedId="@+id/panel_import"
    android:layout="@layout/progress_overlay"
    android:layout_width="fill_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom" />
    
findViewById<View>(R.id.stub_import).visibility = View.VISIBLE
// or
val importPanel: View = findViewById<ViewStub>(R.id.stub_import).inflate()    
```

## tools:visibility / tools:text

通过 `tools:visibility="visible"` 来预览显示的效果，省得再编译运行数据了，方便又提效。
使用 `tools:text` 属性就可以预览字符超限的效果。

## RecyclerView 相关

- tools:listitem

>通过设置 `tools:listitem` 属性来预览 item 的显示效果，`tools:listitem` 属性指定的是一个 layout

```
tools:listitem="@layout/item_main"
```

- tools:itemCount

- tools:listheader

```
tools:listheader="@layout/item_header"
```

- tools:listfooter

```
tools:listfooter="@layout/item_footer"
```

- app:layoutManager

```
app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
android:orientation="horizontal"
```

- app:spanCount

可以指定需要显示的列数。

## app:tint

我们通常会用 ImageView 显示一张图片，比如原本是一个白色的返回 icon，现在另一个地方要用黑色的了，就不需要使用黑白两张图了，
而是使用 tint 来修改为黑色即可，当然，也有局限，适合纯色图片。

代码方式修改 tint：

```
mBinding.imageView.imageTintList = ContextCompat.getColorStateList(this, R.color.greenPrimary)
```

## android:divider

`LinearLayout` 可以通过 `android:divider` 属性添加分割线，结合 `android:showDividers` 属性即可达到效果。

```
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="20dp"
    android:background="@drawable/shape_radius5_white"
    android:orientation="vertical"
    android:divider="@drawable/shape_divider_linear"
    android:showDividers="middle" >

    <TextView
        style="@style/MyTextView"
        android:text="删除个人信息"
        app:drawableStartCompat="@mipmap/ic_helper" />

    <TextView
        style="@style/MyTextView"
        android:text="注销账户"
        app:drawableStartCompat="@mipmap/ic_helper" />

    <TextView
        android:id="@+id/tv_about"
        style="@style/MyTextView"
        android:text="关于我们"
        app:drawableStartCompat="@mipmap/ic_helper" />

</LinearLayout>

<!-- shape_divider_linear.xml -->
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:left="50dp" >
        <shape android:shape="rectangle">
            <solid android:color="#F6F6F6" />
            <size android:height="1dp" />
        </shape>
    </item>
</layer-list>
```

**`showDividers` 有4个选项：**

- middle 每两个组件间显示分隔线

- beginning 开始处显示分隔线

- end 结尾处显示分隔线

- none 不显示

## android:foreground

一般通常会在自定义的 item view 上加上这个属性用来提升用户体验。

```
android:foreground="?android:attr/selectableItemBackground"
```
