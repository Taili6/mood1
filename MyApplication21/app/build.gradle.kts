
plugins {
    alias(libs.plugins.androidApplication)
}
android {
    namespace = "com.example.myapplication"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    packagingOptions {
        exclude ("META-INF/INDEX.LIST" )
        exclude ("META-INF/DEPENDENCIES")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation ("androidx.core:core:1.13.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    //implementation(project(":unityLibrary"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.okhttp)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.logging.interceptor)
    implementation(libs.gson)
    implementation ("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("org.json:json:20210307")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation ("androidx.gridlayout:gridlayout:1.0.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.github.yukuku:ambilwarna:2.0.1")
    implementation ("androidx.webkit:webkit:1.6.0")



}
