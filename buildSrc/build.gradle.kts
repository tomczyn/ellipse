plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("android-application-plugin") {
            id = "android-application-plugin"
            implementationClass = "AndroidApplicationPlugin"
        }
        register("android-library-plugin") {
            id = "android-library-plugin"
            implementationClass = "AndroidLibraryPlugin"
        }
        register("kotlin-library-plugin") {
            id = "kotlin-library-plugin"
            implementationClass = "KotlinLibraryPlugin"
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    implementation("com.android.tools.build:gradle:8.0.0")
    implementation(kotlin("gradle-plugin", "1.8.20"))
    implementation("com.squareup:javapoet:1.13.0")
}
