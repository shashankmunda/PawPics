import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.version

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version("9.4.1") apply(false)
    id("com.google.dagger.hilt.android") version("2.60.1") apply(false)
    id("org.jetbrains.kotlin.android") version("2.3.0") apply(false)
    id("com.google.devtools.ksp") version("2.3.6") apply(false)
    id("org.jetbrains.kotlin.plugin.serialization") version("2.0.21") apply(false)
    id("com.google.gms.google-services") version("4.4.2") apply(false)
    id("com.google.firebase.crashlytics") version("3.0.0") apply(false)
    id("org.jetbrains.kotlin.plugin.compose") version("2.2.10") apply(false)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
}

tasks.register("clean") {
    delete(rootProject.layout.buildDirectory)
}
tasks.register("testClasses")
