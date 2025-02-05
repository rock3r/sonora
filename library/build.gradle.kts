import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.kotlin"
version = "1.0.0"

kotlin {
    applyDefaultHierarchyTemplate()

    jvm {
        compilerOptions.jvmTarget = JvmTarget.JVM_17
    }

    androidTarget {
        publishAllLibraryVariants()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinx.serialization.core)
                api(libs.xmlutil)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(kotlin("test-common"))
            }
        }
        jvmTest {
            dependencies {
                implementation(libs.junit.jupiter.api)
                implementation(kotlin("test-junit5"))
                runtimeOnly(libs.junit.jupiter.engine)
            }
        }
    }
}

android {
    namespace = "dev.sebastiano.sonora"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "library", version.toString())

    pom {
        name = "My library"
        description = "A library."
        inceptionYear = "2024"
        url = "https://github.com/kotlin/multiplatform-library-template/"
        licenses {
            license {
                name = "XXX"
                url = "YYY"
                distribution = "ZZZ"
            }
        }
        developers {
            developer {
                id = "XXX"
                name = "YYY"
                url = "ZZZ"
            }
        }
        scm {
            url = "XXX"
            connection = "YYY"
            developerConnection = "ZZZ"
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// TODO uncomment when you use iOS or use env variables
//  - just Ask Marco or AI
//tasks.register<Copy>("copyiOSTestResources") {
//    from("src/commonTest/resources")
//    into("build/bin/iosX64/debugTest/resources")
//}
//
//tasks.findByName("iosX64Test")!!.dependsOn("copyiOSTestResources")
