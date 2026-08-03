package com.example.polyphonexample;

import vizardalpha.polyphon.api.ConsumerLog;

/**
 * The mod's own logic. Deliberately plain: it has no dependency on Songs of Syx, so it can be
 * unit-tested and reused freely. The {@link com.example.polyphonexample.mixin mixin classes} are
 * the thin layer that connects the game to this code.
 *
 * <p>Keeping the "what to do" (here) separate from the "where to hook" (the mixins) is the
 * recommended shape: mixins stay tiny and declarative, all real behaviour lives in normal classes.
 *
 * <p>Logging goes through {@link ConsumerLog}, Polyphon's shared consumer logger: prefixed,
 * printf-style, never throws, WARN/ERROR on stderr; no hand-rolled prefixes, no logging framework
 * to ship. One instance per name, declared once.
 */
public final class ExampleMod {

    private static final ConsumerLog LOG = ConsumerLog.get("PolyphonExample");

    /** Guards the one-time greeting so we do not spam the console every frame. */
    private static boolean greeted = false;

    private ExampleMod() {
        // static utility; not instantiable
    }

    /**
     * Called from {@link com.example.polyphonexample.mixin.MenuUpdateMixin} on every main-menu tick.
     * Logs a single greeting the first time the menu updates, which proves that our mixin was
     * discovered, injected and is running inside the game.
     */
    public static void onMenuTick() {
        if (greeted) {
            return;
        }
        greeted = true;
        LOG.info("Hello from a Polyphon mixin - the main menu is ticking!");
    }
}
