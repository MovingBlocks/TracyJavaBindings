#define TRACY_DELAYED_INIT
#define TRACY_MANUAL_LIFETIME
#define TRACY_NO_CRASH_HANDLER
#define TRACY_NO_CONTEXT_SWITCH
#define TRACY_NO_SAMPLING
#define TRACY_NO_FRAME_IMAGE
// TODO: Ideally, do not define TRACY_NO_EXIT. Exit gracefully instead.
#define TRACY_NO_EXIT
#define TRACY_ENABLE
#include "TracyClient.cpp"
#include "tracy/TracyC.h"

#include "jni.h"

extern "C"
{
    JNIEXPORT void JNICALL Java_io_github_benjaminamos_tracy_Tracy_startupProfiler(JNIEnv *env, jobject obj)
    {
        ___tracy_startup_profiler();
    }

    JNIEXPORT void JNICALL Java_io_github_benjaminamos_tracy_Tracy_shutdownProfiler(JNIEnv *env, jobject obj)
    {
        ___tracy_shutdown_profiler();
    }

    JNIEXPORT jboolean JNICALL Java_io_github_benjaminamos_tracy_Tracy_isConnected(JNIEnv *env, jobject obj)
    {
        return ___tracy_connected();
    }

    JNIEXPORT void JNICALL Java_io_github_benjaminamos_tracy_Tracy_markFrame(JNIEnv *env, jobject obj)
    {
        ___tracy_emit_frame_mark(NULL);
    }

    JNIEXPORT jlong JNICALL Java_io_github_benjaminamos_tracy_Tracy_allocSourceLocation(JNIEnv *env, jobject obj, jint line, jstring source, jstring function, jstring name, jint colour)
    {
        const char* sourceUTF8 = env->GetStringUTFChars(source, NULL);
        const char* functionUTF8 = env->GetStringUTFChars(function, NULL);
        const char* nameUTF8 = env->GetStringUTFChars(name, NULL);

        uint64_t handle = ___tracy_alloc_srcloc_name(line, sourceUTF8, env->GetStringUTFLength(source), functionUTF8, env->GetStringUTFLength(function), nameUTF8, env->GetStringUTFLength(name), colour);

        env->ReleaseStringUTFChars(source, sourceUTF8);
        env->ReleaseStringUTFChars(function, functionUTF8);
        env->ReleaseStringUTFChars(name, nameUTF8);
        return handle;
    }

    JNIEXPORT jobject JNICALL Java_io_github_benjaminamos_tracy_Tracy_zoneBegin(JNIEnv *env, jobject obj, jlong sourceLocation, int active)
    {
        TracyCZoneCtx zoneContext = ___tracy_emit_zone_begin_alloc(sourceLocation, active);

        jclass zoneContextClass = env->FindClass("io/github/benjaminamos/tracy/Tracy$ZoneContext");
        return env->NewObject(zoneContextClass, env->GetMethodID(zoneContextClass, "<init>", "(II)V"), zoneContext.id, zoneContext.active);
    }

    JNIEXPORT void JNICALL Java_io_github_benjaminamos_tracy_Tracy_zoneEnd(JNIEnv *env, jobject obj, jobject zoneContextObject)
    {
        jclass zoneContextClass = env->FindClass("io/github/benjaminamos/tracy/Tracy$ZoneContext");

        TracyCZoneCtx zoneContext;
        zoneContext.id = env->GetIntField(zoneContextObject, env->GetFieldID(zoneContextClass, "id", "I"));
        zoneContext.active = env->GetIntField(zoneContextObject, env->GetFieldID(zoneContextClass, "active", "I"));
        ___tracy_emit_zone_end(zoneContext);
    }
}