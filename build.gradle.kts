buildscript {
    dependencies {
        classpath ("com.android.tools.build:gradle:8.6.1")  // Đổi 8.7.0 thành 8.6.0
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
}