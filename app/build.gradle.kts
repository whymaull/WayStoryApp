plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.waystoryapp"
    compileSdk = 34

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")
        applicationId = "com.example.waystoryapp"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
//
//    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.core:core-ktx:1.12.0")

//    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.appcompat:appcompat:1.6.1")

//    implementation("com.google.android.material:material:1.10.0")
    implementation("com.google.android.material:material:1.10.0")

//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

//    testImplementation("junit:junit:4.13.2")
    testImplementation("junit:junit:4.13.2")

//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

//
//    // Camera
//    implementation("androidx.camera:camera-view:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")

//
//    // Data store
//    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

//
//    // MVVM
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

//    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.activity:activity-ktx:1.8.0")

//
//    // Retrofit
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

//    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

//    // Glide
//    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

//
//    // Maps
//    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")

//    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")

//    implementation("com.google.android.gms:play-services-places:17.0.0")
        implementation("com.google.android.gms:play-services-places:17.0.0")


//
//    // Paging
//    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.paging:paging-runtime-ktx:3.1.0")

//    implementation("androidx.room:room-paging:2.4.0-rc01")
    implementation("androidx.room:room-paging:2.6.0")

//    implementation ("androidx.room:room-ktx:2.6.0")
    implementation("androidx.room:room-ktx:2.4.2")


//    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-runtime:2.4.2")

//    kapt("androidx.room:room-compiler:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")

//
//    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.arch.core:core-testing:2.1.0") // InstantTaskExecutorRule

//    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1") //TestDispatcher

//    testImplementation("org.mockito:mockito-core:3.12.4")
    testImplementation("org.mockito:mockito-core:3.12.4")

//    testImplementation("org.mockito:mockito-inline:3.12.4")
    testImplementation("org.mockito:mockito-inline:3.12.4")


}