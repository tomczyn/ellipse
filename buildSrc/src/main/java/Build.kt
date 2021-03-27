import com.android.build.gradle.BaseExtension

private const val RELEASE = "release"
private const val DEBUG = "debug"

fun BaseExtension.buildTypesConfig() {
    buildTypes {
        getByName(RELEASE) {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName(DEBUG) { }
    }
}
