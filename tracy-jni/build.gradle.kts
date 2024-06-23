import org.gradle.internal.jvm.Jvm

// Copyright 2024 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

plugins {
    `cpp-library`
}

version = "1.0.0"

library {
    source.from(file("src"))
    privateHeaders.from(
        file("src"), file("tracy/public"),
        file("${Jvm.current().javaHome}/include"),
        file("${Jvm.current().javaHome}/include/win32"),
        file("${Jvm.current().javaHome}/include/darwin"),
        file("${Jvm.current().javaHome}/include/linux")
    )
    publicHeaders.from(file("include"))
}
