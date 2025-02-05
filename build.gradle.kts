import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest
import org.jetbrains.kotlin.gradle.tasks.KotlinTest

plugins {
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinMultiplatform) apply  false
    alias(libs.plugins.vanniktech.mavenPublish) apply false
}

allprojects {
    tasks.withType<Test>().configureEach {
        environment("KAREN_MODE", project.file("src/commonTest/resources").absolutePath)
    }

//    tasks.withType<KotlinNativeTest>().configureEach {
//        environment("SIMCTL_CHILD_ZIPLINE_ROOT", rootDir)
//        environment("ZIPLINE_ROOT", rootDir)
//    }
//
//    tasks.withType<KotlinJsTest>().configureEach {
//        environment("ZIPLINE_ROOT", rootDir.toString())
//    }
}
