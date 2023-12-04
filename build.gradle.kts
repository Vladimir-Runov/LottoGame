plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "vr.runov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    testImplementation(kotlin("test"))
//    compile "org.jetbrains.kotlinx:kotlinx-coroutines-core:0.15"/

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1")
 //   implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.0.1")
//                   "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1"
//    implementation fileTree(dir: "libs", include: ["*.jar", '*.aar'])
//    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
//    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
//    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0'
//    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.4.0'
//    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.4.0'
//    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines_version"
//    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines_version"
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}