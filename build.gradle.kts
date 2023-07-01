// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0-rc01" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    kotlin("jvm") version "1.8.0" apply false
    id("com.android.library") version "8.1.0-rc01" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}

buildscript {
    val hiltVersion = "2.46.1"
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.8.0"))
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
    }
}
