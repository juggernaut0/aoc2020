plugins {
    id("org.jetbrains.kotlin.js") version "1.4.10"
}

repositories {
    mavenCentral()
    maven("https://juggernaut0.github.io/m2/repository")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("com.github.juggernaut0.kui:kui:0.11.0")
}

kotlin {
    js {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        binaries.executable()
    }
}