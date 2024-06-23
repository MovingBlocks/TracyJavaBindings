// Copyright 2024 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package io.github.benjaminamos.tracy;

import org.junit.jupiter.api.Test;

public class TracyTest {
    @Test
    public void testStartup() {
        Tracy.startupProfiler();

        System.out.println("Waiting for profiler...");
        if (!Tracy.isConnected()) {
            try {
                this.wait(5000);
            } catch (Throwable ignore) {
            }

            if (!Tracy.isConnected()) {
                System.out.println("Profiler connect timeout exceeded.");
                return;
            }
        }

        long handle = Tracy.allocSourceLocation(0, "Test.java", "test()", "Test!", 0);
        Tracy.ZoneContext zoneContext = Tracy.zoneBegin(handle, 1);

        try {
            this.wait(5000);
        } catch (Throwable ignore) {
        }

        Tracy.zoneEnd(zoneContext);

        Tracy.markFrame();

        Tracy.shutdownProfiler();
    }
}
