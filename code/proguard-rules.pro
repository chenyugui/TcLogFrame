# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 第三方库 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
# AVLoader
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }
# GreenDao ↓↓↓
-dontwarn org.greenrobot.greendao.**
-keep class org.greenrobot.greendao.** { *; }
-dontwarn freemarker.**
-keep class freemarker.** { *; }
# GreenDao ↑↑↑

# 第三方库 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

# Bean类 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
# ItemBean
-keep class com.taichuan.code.ui.loadmoreview.bean.** { *; }
-keep class com.taichuan.code.ui.loadmoreview.bean.** { *; }
# Bean类 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑