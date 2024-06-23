// Copyright 2024 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package io.github.benjaminamos.tracy;

import java.nio.file.Paths;

public final class Tracy {
    private Tracy() {
    }

    static {
        String libraryPath = System.getProperty("org.terasology.librarypath");
        if (libraryPath == null) {
            System.loadLibrary("tracy-jni");
        } else {
            System.load(Paths.get(libraryPath + "/tracy-jni.dll").toAbsolutePath().toString());
        }
    }

    public static native void startupProfiler();
    public static native void shutdownProfiler();
    public static native boolean isConnected();
    public static native void markFrame();
    public static native long allocSourceLocation(int line, String source, String function, String name, int colour);
    public static native ZoneContext zoneBegin(long sourceLocation, int active);
    public static native void zoneEnd(ZoneContext zoneContext);

    public static final class ZoneContext {
        public final int id;
        public final int active;

        public ZoneContext(int id, int active) {
            this.id = id;
            this.active = active;
        }
    }
}
