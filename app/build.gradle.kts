plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.assignment.enrollpro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.assignment.enrollpro"
        minSdk = 26
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity.v182)
    implementation(libs.firebase.dynamic.links)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    /* Sweet Alerts */
    implementation("com.github.f0ris.sweetalert:library:1.6.2")

    /* ImageViews */
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.squareup.picasso:picasso:2.71828")


    implementation(libs.recyclerview)

    /*Maps*/
    implementation (libs.play.services.maps)
    implementation (libs.play.services.places)
    implementation (libs.play.services.location)

    implementation ("com.skyfishjy.ripplebackground:library:1.0.1")
    /*Maps*/

//    implementation("me.dm7.barcodescanner:zxing:1.9.13")
    implementation("com.karumi:dexter:6.2.2")

    implementation ("com.journeyapps:zxing-android-embedded:4.1.0")
//    implementation ("com.opencsv:opencsv:5.9")
}