package com.rko

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class SkinButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatButton(context, attrs), SkinSupportable {

    override fun applySkin(skinApkPath: String) {
        // 获取皮肤包的 packageInfo
        val skinPackageInfo =
            context.packageManager.getPackageArchiveInfo(skinApkPath, PackageManager.GET_ACTIVITIES)
                ?.apply {
                    with(applicationInfo) {
                        sourceDir = skinApkPath
                        publicSourceDir = skinApkPath
                    }
                }

        // 获取皮肤包的 Resources
        var skinRes: Resources? = null
        skinPackageInfo?.let {
            skinRes = context.packageManager.getResourcesForApplication(it.applicationInfo)
        }

        // 获取 app 的 Resources 用于获取 displayMetrics、configuration
        val appRes = context.resources

        // 构造出新的皮肤包 Resources； skinRes.assets 是皮肤包的 AssetManager
        val newRes = Resources(skinRes?.assets, appRes.displayMetrics, appRes.configuration)

        // 当前按钮默认背景色
        val defaultResId = R.color.colorPrimary

        /** 制作皮肤包，这里让皮肤包和 App 本身资源名相同，值不同，这样换肤时，根据 View 设置的资源名去皮肤包中找同名资源。 */
        val resName = appRes.getResourceEntryName(defaultResId)
        val resType = appRes.getResourceTypeName(defaultResId)

        // 从皮肤包中寻找同名资源的 id
        val skinResId = newRes.getIdentifier(resName, resType, skinPackageInfo?.packageName)

        // 通过资源 id 在皮肤包的 Resources 中寻找 color
        val newColor = newRes.getColor(skinResId)
        setBackgroundColor(newColor)
    }
}