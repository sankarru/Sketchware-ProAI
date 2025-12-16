# AndroidManifest.xml Updates Required

## Add Internet Permission

The AI Mode requires internet access for API communication. Add this permission to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Complete Permissions Section

Your `<manifest>` tag should include:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.sketchwarepro">

    <!-- Existing permissions -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <!-- ... other existing permissions ... -->
    
    <!-- NEW: Internet permission for AI Mode -->
    <uses-permission android:name="android.permission.INTERNET" />

    <application>
        <!-- Your existing activities and services -->
        
        <!-- Optionally register AboutTeamActivity if replacing existing About screen -->
        <activity
            android:name="pro.sketchware.ai.ui.AboutTeamActivity"
            android:label="@string/about_team"
            android:theme="@style/AppTheme" />
        
    </application>

</manifest>
```

## Location in File

- Permissions should be placed **before** the `<application>` tag
- Internet permission must be there for any AI API calls to work

## Why This Permission?

- **INTERNET**: Required for ChatGPT and Gemini API calls
- Without this, all AI Mode features will fail with network errors

## Testing

After adding the permission:

1. Rebuild the app
2. Grant permissions when prompted on Android 6.0+
3. Go to Settings → AI Settings
4. Add API keys
5. Create an AI Mode project
6. Test the chat functionality

## Gradle Requirements

Ensure your `build.gradle` has:

```gradle
dependencies {
    // Existing dependencies
    
    // For JSON parsing (if not already present)
    implementation 'org.json:json:20231013'
}
```

If you're already using a JSON library or have it through another dependency, you don't need to add it again.

## Proguard Configuration (if applicable)

If you use Proguard/R8, add to your `proguard-rules.pro`:

```proguard
# Preserve AI classes
-keep class pro.sketchware.ai.** { *; }
-keepclassmembers class pro.sketchware.ai.** { *; }

# Preserve inner classes
-keepclasseswithmembernames class pro.sketchware.ai.** {
    *;
}

# Keep JSON library
-keep class org.json.** { *; }
```
