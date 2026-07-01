#include <jni.h>
#include <string.h>
#include <stdlib.h>

/*
 * FeelWire — Layer 3: NDK credential protection
 * Credentials are XOR-obfuscated with key 0x5A and stored as byte arrays.
 * Even after APK decompilation + .so disassembly, no plaintext string is visible.
 */

static const unsigned char XK = 0x5A;

/* APP_HASH "432ec7d053fc19e15f78b7db0e305f8b" XORed with 0x5A */
static const unsigned char EH[] = {
    0x6e,0x69,0x68,0x3f,0x39,0x6d,0x3e,0x6a,
    0x6f,0x69,0x3c,0x39,0x6b,0x63,0x3f,0x6b,
    0x6f,0x3c,0x6d,0x62,0x38,0x6d,0x3e,0x38,
    0x6a,0x3f,0x69,0x6a,0x6f,0x3c,0x62,0x38
};

/* APP_ID 37418913 split into 4 bytes, each XORed with 0x5A */
static const unsigned char EI[] = { 0x58, 0x60, 0xad, 0xfb };

JNIEXPORT jstring JNICALL
Java_org_telegram_messenger_NativeKeys_getHash(JNIEnv *env, jclass clazz) {
    int len = (int)(sizeof(EH) / sizeof(EH[0]));
    char *buf = (char *)malloc(len + 1);
    if (!buf) return (*env)->NewStringUTF(env, "");
    for (int i = 0; i < len; i++) buf[i] = (char)(EH[i] ^ XK);
    buf[len] = '\0';
    jstring result = (*env)->NewStringUTF(env, buf);
    free(buf);
    return result;
}

JNIEXPORT jint JNICALL
Java_org_telegram_messenger_NativeKeys_getId(JNIEnv *env, jclass clazz) {
    return (jint)(
        ((EI[0] ^ XK) << 24) |
        ((EI[1] ^ XK) << 16) |
        ((EI[2] ^ XK) <<  8) |
         (EI[3] ^ XK)
    );
}
