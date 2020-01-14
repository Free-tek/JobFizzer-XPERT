# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/karthik/Library/Android/sdk/tools/proguard/proguard-android.txt
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
### PROGUARD RULE FOR EVENT BUS
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

### PROGUARD RULE FOR OKHTTP
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform


### PROGUARD RULE FOR FABRIC
-keepattributes *Annotation*
#in order to provide the most meaningful crash reports,
#add the following line to your configuration file:
-keepattributes SourceFile,LineNumberTable
#If you are using custom exceptions,
#add this line so that custom exception types are skipped during obfuscation:
-keep public class * extends java.lang.Exception
#For Fabric to properly de-obfuscate your crash reports, you need to remove this line from your configuration file
-printmapping mapping.txt

### PROGUARD RULE FOR FABRIC
-keep class com.google.gson.** { *; }