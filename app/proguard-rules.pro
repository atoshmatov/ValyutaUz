-keepclassmembers class <1> {
   static <1>$Companion Companion;
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

-keepnames class kotlinx.** { *; }
-keepnames class com.squareup.moshi.** { *; }
-keepnames class com.google.gson.** { *; }

-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type
-keep public class * implements uz.toshmatov.core.mapper.Mapper

-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-keep,allowobfuscation @interface com.google.gson.annotations.SerializedName

# Retrofit
-keep class retrofit2.** { *; }

# OkHttp
-keep class okhttp3.** { *; }

# Gson
-keep class com.google.gson.** { *; }

-keepattributes Signature
-keepattributes *Annotation*
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-keep class **JsonAdapter { *; }
-keep class **JsonAdapter$* { *; }

# Keep all classes in the rustcomponents package
-keep class org.matrix.rustcomponents.sdk.crypto.** { *; }

# Keep constructors for all nested classes
-keepclassmembers class org.matrix.rustcomponents.sdk.crypto.** {
    public <init>();
}

# Keep Realm classes and methods
-keep class io.realm.** { *; }
-keepattributes *Annotation*,EnclosingMethod

# Keep Realm model classes
-keep class your.package.name.model.** extends io.realm.RealmObject { *; }

-dontwarn java.awt.**
-dontwarn javax.swing.**

# Keep JNA classes
-keep class com.sun.jna.** { *; }
-keep class com.sun.jna.* { *; }
-keepclassmembers class * extends com.sun.jna.Structure {
    <fields>;
}
-keepclassmembers class * extends com.sun.jna.Callback {
    public <methods>;
}
-keepattributes Signature

-keep class javax.crypto.** { *; }
-keep class java.security.** { *; }
-keep class android.security.keystore.** { *; }
-keep class org.bouncycastle.** { *; }

# Keep generic Kotlin metadata for reflection
-keepattributes KotlinMetadata

# BMA: Not sure I can delete this one without side effect
-keepattributes *Annotation*

### MOSHI ###
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}

-keep @com.squareup.moshi.JsonQualifier interface *

# Enum field names are used by the integrated EnumJsonAdapter.
# values() is synthesized by the Kotlin compiler and is used by EnumJsonAdapter indirectly
# Annotate enums with @JsonClass(generateAdapter = false) to use them with Moshi.
-keepclassmembers @com.squareup.moshi.JsonClass class * extends java.lang.Enum {
    <fields>;
    **[] values();
}

-keep class kotlin.reflect.jvm.internal.impl.builtins.BuiltInsLoaderImpl

-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

### OKHTTP for Android Studio ###
-keep class okhttp3.Headers { *; }
-keep interface okhttp3.Interceptor.* { *; }

### Webrtc
-keep class org.webrtc.** { *; }

### Serializable persisted classes
# https://www.guardsquare.com/en/products/proguard/manual/examples#serializable
-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-dontwarn com.fasterxml.jackson.databind.**
-dontwarn com.google.android.play.core.splitcompat.SplitCompatApplication
-dontwarn com.google.android.play.core.splitinstall.SplitInstallException
-dontwarn com.google.android.play.core.splitinstall.SplitInstallManager
-dontwarn com.google.android.play.core.splitinstall.SplitInstallManagerFactory
-dontwarn com.google.android.play.core.splitinstall.SplitInstallRequest$Builder
-dontwarn com.google.android.play.core.splitinstall.SplitInstallRequest
-dontwarn com.google.android.play.core.splitinstall.SplitInstallSessionState
-dontwarn com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
-dontwarn com.google.android.play.core.tasks.OnFailureListener
-dontwarn com.google.android.play.core.tasks.OnSuccessListener
-dontwarn com.google.android.play.core.tasks.Task