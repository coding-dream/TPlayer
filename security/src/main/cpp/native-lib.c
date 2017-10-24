#include <jni.h>
#include <string.h>

JNIEXPORT jstring JNICALL
Java_com_less_security_Security_stringFromJNI(JNIEnv *env, jobject obj) {
    return (*env)->NewStringUTF(env, "I am C Language");
}