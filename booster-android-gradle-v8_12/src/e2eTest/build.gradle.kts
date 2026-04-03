plugins {
    id("com.android.application") version "8.12.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("com.didiglobal.booster") version "1.0.0-SNAPSHOT" apply false
}

buildscript {
    dependencies {
        classpath("com.didiglobal.booster:booster-android-gradle-compat:1.0.0-SNAPSHOT")
        classpath("com.didiglobal.booster:booster-android-gradle-v8_12:1.0.0-SNAPSHOT")
        classpath("com.didiglobal.booster:booster-transform-shared-preferences:1.0.0-SNAPSHOT")
        classpath("com.didiglobal.booster:booster-transform-thread:1.0.0-SNAPSHOT")
    }
}
