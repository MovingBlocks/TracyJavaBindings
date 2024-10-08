// Copyright 2024 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

plugins {
    id("java-library")
}

group "io.github.benjaminamos.TracyJavaBindings"
version "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

sourceSets {
    main
}

tasks.withType<Jar>().configureEach {
    val sharedLibraryTasks = project(":tracy-jni").tasks.withType<LinkSharedLibrary>()
    dependsOn(sharedLibraryTasks)
    val sharedLibraryOutputs = sharedLibraryTasks.find { task ->
        task.name.toLowerCase().contains("release")
    }?.outputs?.files?.asFileTree?.files
    sharedLibraryOutputs?.let {
        from(it) {
            include("*.dll")
            into("windows")
        }
        from(it) {
            include("*.so")
            into("linux")
        }
        from(it) {
            include("*.dylib")
            into("macosx")
        }
    }
    from(File(project(":tracy-jni").projectDir, "tracy")) {
        include("LICENSE")
        rename("LICENSE", "TRACY_LICENSE")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    maxHeapSize = "1G"

    testLogging {
        events("passed")
    }

    systemProperty("java.library.path", project(":tracy-jni").layout.buildDirectory.dir("lib/main/debug").get().asFile.absolutePath)
}
