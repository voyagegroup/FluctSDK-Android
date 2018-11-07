# region https://github.com/voyagegroup/FluctSDK-Android/wiki/%E5%8B%95%E7%94%BB%E3%83%AA%E3%83%AF%E3%83%BC%E3%83%89%E5%BA%83%E5%91%8A%E3%81%AE%E5%B0%8E%E5%85%A5%E6%96%B9%E6%B3%95#fluctsdk
-keep class jp.fluct.mediation.** { *; }
# endregion

# region https://monetization-support.applovin.com/hc/ja/articles/236114548-Proguard-Config%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB%E3%81%AB%E4%BD%95%E3%82%92%E5%8A%A0%E3%81%88%E3%82%8B%E5%BF%85%E8%A6%81%E3%81%8C%E3%81%82%E3%82%8A%E3%81%BE%E3%81%99%E3%81%8B-
-dontwarn com.applovin.**
# -libraryjars libs/applovin-sdk-6.3.0.jar
-keep class com.applovin.** { *; }
-keep class com.google.android.gms.ads.identifier.** { *; }
# endregion

# region https://github.com/fan-ADN/nendSDK-Android/wiki/Proguard-%E3%81%AE%E8%A8%AD%E5%AE%9A
-keep class net.nend.android.** { *; }
-dontwarn net.nend.android.**
# endregion

# region https://github.com/Unity-Technologies/unity-ads-android/wiki/sdk_android_integration_guide#integrating-without-android-studio
# Keep filenames and line numbers for stack traces
-keepattributes SourceFile,LineNumberTable

# Keep JavascriptInterface for WebView bridge
-keepattributes JavascriptInterface

# Sometimes keepattributes is not enough to keep annotations
-keep class android.webkit.JavascriptInterface {
   *;
}

# Keep all classes in Unity Ads package
-keep class com.unity3d.ads.** {
   *;
}
# endregion

# region AdColony https://github.com/AdColony/AdColony-Android-SDK-3/wiki/Project-Setup#step-4-proguard-configuration
# For communication with AdColony's WebView
# -keepclassmembers class * {
#     @android.webkit.JavascriptInterface <methods>;
# }
# Keep ADCNative class members unobfuscated
-keepclassmembers class com.adcolony.sdk.ADCNative** {
*;
}
# For removing warnings due to lack of Multi-Window support
# -dontwarn android.app.Activity
# endregion

# region Tapjoy https://dev.tapjoy.com/sdk-integration/android/getting-started-guide-publishers-android/#toc_add-app-permissions-and-activities
-keep class com.tapjoy.** { *; }
-keep class com.moat.** { *; }
-keepattributes JavascriptInterface
-keep class * extends java.util.ListResourceBundle {
protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
@com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
public static final ** CREATOR;
}
-keep class com.google.android.gms.ads.identifier.** { *; }
-dontwarn com.tapjoy.**
# endregion

# region android support
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-dontwarn android.support.**
# endregion
