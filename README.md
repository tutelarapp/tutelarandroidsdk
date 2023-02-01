# Tutelar SDK

Tutelar are risk management experts. Our high powered tech-enabled solutions simplify fraud risk management and automates auxiliary business processes enabling companies to focus on its core products and services.

|             | Android       |
|-------------|---------------|
| **Support** | minSdk 21+    |
|             | targetSdk 33+ |

## Getting Started

Add the dependency in the app level build.gradle:
```
dependencies {
	        implementation 'com.github.tutelarapp:tutelarandroidsdk:-SNAPSHOT'
	}
```

After that you can access this Tutelarfluttersdk class like the below: (Pass the IP address in this method)
```Dart
deviceInfo = await _sdkplugin.getDeviceInfo(ip_address);

```


## Requirements
### Android
This plugin requires several changes to be able to work on Android devices. Please make sure you follow all these steps:
1. Use Android 5.0 (API level 21) and above.
2. Use Kotlin version 1.6.10 and above:  example ext.kotlin_version = '1.6.10'
3. Using an up-to-date Android gradle build tools version and an up-to-date gradle version accordingly.
4. Rebuild the app, as the above changes don't update with hot reload
   These changes are needed.

