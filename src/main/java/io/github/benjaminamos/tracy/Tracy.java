// Copyright 2024 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package io.github.benjaminamos.tracy;

import java.io.File;

public final class Tracy {
    private Tracy() {
    }

    static {
        String libraryPath = System.getProperty("org.terasology.librarypath");
        if (libraryPath == null) {
            System.loadLibrary("tracy-jni-" + System.getProperty("os.arch"));
        } else {
            File libraryDirectory = new File(libraryPath);
            if (libraryDirectory.exists() && libraryDirectory.isDirectory()) {
                String architecture = System.getProperty("os.arch");
                for (File file : libraryDirectory.listFiles()) {
                    if (file.getName().startsWith("tracy-jni-" + architecture) || file.getName().startsWith("libtracy-jni-" + architecture)) {
                        System.load(file.getPath());
                    }
                }
            }
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
