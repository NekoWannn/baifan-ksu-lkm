plugins {
    alias(libs.plugins.agp.app) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.compose.compiler) apply false
}

extra["androidMinSdkVersion"] = 31
extra["androidTargetSdkVersion"] = 37
extra["androidCompileSdkVersion"] = 37
extra["androidCompileSdkVersionMinor"] = 0
extra["androidBuildToolsVersion"] = "36.0.0"
extra["androidCompileNdkVersion"] = libs.versions.ndk.get()
extra["androidSourceCompatibility"] = JavaVersion.VERSION_21
extra["androidTargetCompatibility"] = JavaVersion.VERSION_21
extra["managerVersionCode"] = getVersionCode()
extra["managerVersionName"] = getVersionName()

fun getGitCommitCount(): Int = runCatching {
    val process = Runtime.getRuntime().exec(arrayOf("git", "rev-list", "--count", "HEAD"))
    process.inputStream.bufferedReader().use { it.readText().trim().toInt() }
}.getOrDefault(2680)

fun getGitDescribe(): String = runCatching {
    val process = Runtime.getRuntime().exec(arrayOf("git", "describe", "--tags", "--always"))
    process.inputStream.bufferedReader().use { it.readText().trim() }
}.getOrDefault("v3.3.1-baifan.13")

fun getVersionCode(): Int {
    val commitCount = getGitCommitCount()
    return 30000 + commitCount
}

fun getVersionName(): String {
    return getGitDescribe()
}
