TextView 中有三个属性可以设置字体的显示：

textStyle/ typeface/ fontFamily

### textStyle

```
<!-- textStyle 是用 flag 来承载的，flag 表示的值可以做或运算，也就是说我们可以设置多种字体样式进行叠加 -->
<attr name="textStyle">
    <flag name="normal" value="0" />
    <flag name="bold" value="1" />
    <flag name="italic" value="2" />
</attr>

android:textStyle="bold|italic"
```

### typeface

typeface 主要用于设置 TextView 的字体

```
//TextView 的自定义属性 typeface
<attr name="typeface">
    <enum name="normal" value="0" />
    <enum name="sans" value="1" />
    <enum name="serif" value="2" />
    <enum name="monospace" value="3" />
</attr>
```

- noraml：普通字体，系统默认使用的字体

- sans：非衬线字体

- serif：衬线字体

- monospace：等宽字体

typeface 是用 enum 来承载的，enum 表示枚举类型，每次只能选择一个，因此我们每次只能设置一种字体，不能叠加。

同样我们也可以在代码中进行设置：

```
mTv.setTypeface(Typeface.SERIF)
```

### fontFamily

fontFamily 相当于是加强版的 typeface，它表示 android 系统支持的一系列字体，
fontFamily 接收的是一个 String 类型的值，也就是我们可以通过字体别名设置这种字体。

```
//TextView 的自定义属性 fontFamily
<attr name="fontFamily" format="string" />
```

代码设置：

```
mTv.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL))
```

值的注意的是：fontFamily 设置的某些字体有兼容性问题，如我上面设置的 sans-serif-medium 字体，它在 Android 系统版本大于等于 21 才会生效，
如果小于 21 ，则会使用默认字体，因此我们在使用 fontFamily 属性时，需要注意这个问题。

最后对这三个属性做一个总结：

1、fontFamily、typeface 属性用于字体设置，如果都设置了，优先使用 fontFamily 属性，typeface 属性不会生效

2、textStyle 用于字体样式设置，与字体设置不会产生冲突

## 项目需求：全局替换当前项目中的默认字体，并引入 UI 设计师提供的一些新字体 ???

- 方式一：通过遍历 ViewTree，全局替换字体

>这种方式会遍历 Xml 文件中的所有 View 和 ViewGroup，但是如果出现 RecyclerView , ListView，或者其他 ViewGroup 里面动态添加 View，
那么我们还是需要去手动添加替换的逻辑，否则字体不会生效。而且它每次递归遍历 ViewTree，性能上多少会有点影响。

- 方式二：通过 LayoutInflater，利用自定义的 Factory 全局替换字体

- 通过配置应用主题，全局替换默认字体
































