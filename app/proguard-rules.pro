#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#不预校验
-dontpreverify
#忽略警告
-ignorewarnings

#-libraryjars ./libs/android-support-v4.jar
#-libraryjars ./libs/butterknife-7.0.1.jar
#-libraryjars ./libs/fastjson-android-1.1.34.jar
#-libraryjars ./libs/locSDK_5.2.jar
#-libraryjars ./libs/okhttp-2.2.0.jar
#-libraryjars ./libs/picasso-2.5.0.jar
#-libraryjars ./libs/ormlite-android-4.49.jar
#-libraryjars ./libs/ormlite-core-4.49.jar

#####################记录生成的日志数据,gradle build时在本项目根目录输出################
# apk 包内所有 class 的内部结构
-dump class_files.txt
# 未混淆的类和成员
-printseeds seeds.txt
# 列出从 apk 中删除的代码
-printusage unused.txt
# 混淆前后的映射
-printmapping mapping.txt
#####################记录生成的日志数据，gradle build时 在本项目根目录输出################
##################below is for common android
-keep public class **.R$* { public static final int *; }
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.view.View
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep class * implements android.os.Parcelable {public static final android.os.Parcelable$Creator *; }

#保护注解
-keepattributes *Annotation*
#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature
#如果有引用v4包可以添加下面这行
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
#如果引用了v4或者v7包，可以忽略警告，因为用不到android.support
-dontwarn android.support.**
#忽略警告
-ignorewarning
# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
# 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}
# 保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
# 保持 Serializable 不被混淆并且enum 类也不被混淆
# 不混淆资源类
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}
##################upper is for common android

-keep public interface com.yuzhi.fine.common.NotObfuscateInterface{public *;}
-keep class * implements com.yuzhi.fine.common.NotObfuscateInterface{
	<methods>;
	<fields>;
}

# OrmLite uses reflection
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }


# butterknife uses reflection
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions