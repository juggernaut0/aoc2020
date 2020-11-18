plugins {
    id("org.jetbrains.kotlin.js") version "1.4.10"
    id("com.bmuschko.docker-remote-api") version "6.1.3"
}

version = "1.0-SNAPSHOT"

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

tasks {
    val dockerCopyDist by registering(Copy::class) {
        dependsOn("browserDistribution")
        from("$buildDir/distributions")
        into("$buildDir/docker/app")
    }

    val dockerfile by registering(com.bmuschko.gradle.docker.tasks.image.Dockerfile::class) {
        dependsOn(dockerCopyDist)

        from("nginx:1.19-alpine")
        addFile("app", "/usr/share/nginx/html")
    }

    val dockerBuild by registering(com.bmuschko.gradle.docker.tasks.image.DockerBuildImage::class) {
        dependsOn(dockerfile)

        if (version.toString().endsWith("SNAPSHOT")) {
            images.add("${rootProject.name}:SNAPSHOT")
        } else {
            images.add("juggernaut0/${rootProject.name}:$version")
        }
    }
}