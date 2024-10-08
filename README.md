## TracyJavaBindings – Java bindings for the Tracy Profiler
### Prerequisites
- After cloning the repository, ensure that you have checked-out submodules with `git submodule update --init`.
### Building
#### JNI Bindings
- You can build the JNI bindings with `gradlew jar`
#### Tracy profiler GUI
> _All commands need to be run in the `tracy-jni/tracy` subdirectory._
- Create a new `build` folder under `tracy-jni/tracy/profiler`.
- Configure the profiler using `cmake -B profiler/build -S profiler -DCMAKE_BUILD_TYPE=Release` (this only needs to be done once).
- Build the profiler using `cmake --build profiler/build --parallel --config Release`.
- The built `tracy-profiler` executable can be found in the `profiler/build/Release` directory.

### Integration
- At runtime, the `java.library.path` system property should contain a path to the native JNI libraries found in `tracy-jni/build/lib/main/release`.
### Usage Example
```java
import io.github.benjaminamos.tracy.Tracy;

public class TracyTest {
    public static void main(String[] args) {
        // Start profiling
        Tracy.startupProfiler();

        // Allocate and begin zone
        long handle = Tracy.allocSourceLocation(0, "Test.java", "test()", "Test!", 0);
        Tracy.ZoneContext zoneContext = Tracy.zoneBegin(handle, 1);

        // Do work...

        // End zone
        Tracy.zoneEnd(zoneContext);

        // Begin new frame
        Tracy.markFrame();

        // Stop profiling
        Tracy.shutdownProfiler();
    }
}
```