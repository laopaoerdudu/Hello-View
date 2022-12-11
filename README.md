## LayoutInflater

```
public View inflate(int resource, 
                    @Nullable ViewGroup root, 
                    boolean attachToRoot) {
    ...
}
```

我们知道，Android 的布局结构是一种树状结构。每个布局都可以包含若干个子布局，每个子布局又可以继续包含子布局，以此构建出任意样式的 View 呈现给用户。
因此，我们大致可以明白，每个布局它都是要有一个父布局的。

1，inflate() 方法第二个参数 root 的作用，就是给当前要加载的 xml 布局指定一个父布局。

>那么一个布局可不可以没有父布局呢？当然也是可以的，这也是为什么 root 参数被标为 `@Nullable` 的原因。

但是如果我们 inflate 出来了一个没有父布局的布局，又该如何去展示它呢？
那自然是没有办法去展示的，所以只能后面再用 addView 的方式将它添加到某个现有的布局下面。
又或者你 inflate 出来的布局就是个顶层布局，所以它不需要有父布局。
但是这些场景都比较少见，因此大多数情况下，我们在使用 `LayoutInflater 的 inflate()` 方法时都是要指定父布局的。

我们做如下的测试：

1，以 addView 的形式加载 button_layout.xml

2，将按钮的宽高指定成了300dp，高度指定成了100dp

发现不管你将 Button 的 layout_width 和 layout_height 的值修改成多少，都不会有任何效果的，
因为这两个值现在已经完全失去了作用。
平时我们经常使用 layout_width 和 layout_height 来设置 View 的大小，并且一直都能正常工作，
而实际上则不然，它们其实是用于设置 View 在布局中的大小的，
也就是说，首先 View 必须存在于一个布局中才行。这也是为什么这两个属性叫作 `layout_width`和 `layout_height`，
而不是 `width` 和 `height`。

而我们因为在使用 LayoutInflater 加载 button_layout.xml 这个布局时并没有为它指定父布局，
因此这里 layout_width 和 layout_height 属性就都失去了作用。
更准确点来讲，所有以 layout_ 开头的属性都会失去作用。

## attachToRoot

`attachToRoot` 的意思，就是在问我们要不要将当前加载的xml布局添加到第二个参数传入的父布局上面。
如果传入true，那么就意味着会添加，传入false就表示不会添加。

在 Fragment 中加载一个布局我们通常都会这么写：

```
public class MyFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                             @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout, container, false);
    }
}
```

**为什么这里的 attachToRoot 设为 false 呢？**

观察一下 Fragment 的相关源码，你会发现它会将我们在 onCreateView() 方法中返回的 View 添加到一个 Container 当中：

```
void addViewToContainer() {
    // Ensure that our new Fragment is placed in the right index
    // based on its relative position to Fragments already in the
    // same container
    int index = mFragmentStore.findFragmentIndexInContainer(mFragment);
    mFragment.mContainer.addView(mFragment.mView, index);
}
```

除了 Fragment 之外，RecyclerView 中对于 LayoutInflater 的用法也是基于一模一样的原因，这里就不再展开讨论了。

Ref:

https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650260651&idx=1&sn=54558af601a2cd10a04b7c070b64d3c9&chksm=886335c4bf14bcd2b4462692246e75c5e47bd3a9d9be533a7b4093b7dd2ab5d3524568fecb0c&cur_album_id=1455589563531214850&scene=189#wechat_redirect





