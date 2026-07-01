package org.telegram.messenger;

/**
 * FeelWire — Layer 2: Java bridge to NDK credential store.
 * Actual values live in feelwire_keys.c as XOR-obfuscated byte arrays.
 * This class is intentionally minimal — no fallback, no logging.
 */
public final class NativeKeys {

    static {
        System.loadLibrary("tmessages.49");
    }

    public static native String getHash();
    public static native int    getId();

    private NativeKeys() {}
}
