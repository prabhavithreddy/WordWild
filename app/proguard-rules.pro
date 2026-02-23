# ReadiKids ProGuard Rules

# Keep Room entities
-keep class com.readikids.app.data.model.** { *; }

# Keep Hilt
-keep class dagger.hilt.** { *; }

# Keep Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Keep Kotlin coroutines
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Keep Compose
-keep class androidx.compose.** { *; }
