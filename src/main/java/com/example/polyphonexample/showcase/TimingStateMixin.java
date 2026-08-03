package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.At;
import vizardalpha.polyphon.api.Inject;
import vizardalpha.polyphon.api.Mixin;
import vizardalpha.polyphon.api.State;

/**
 * {@link State}: per-invocation scratch space shared between a HEAD and a RETURN handler on the same
 * method. Each call gets its own {@code State} (it lives in a local of the target method), so it is
 * inherently thread- and reentrancy-safe. A {@code State} parameter sits where the framework lead
 * parameters go: {@code (State)}, {@code (self, State)}, or {@code (self, State, <target args>)}.
 *
 * <p>Classic use: timing a method. HEAD stores the start, RETURN reads it back and reports the elapsed.
 *
 * <p>Verified against songsofsyx 71.44: {@code world.WORLD.initBeforePlay()} is a one-shot world
 * init, exactly where timing is meaningful. The name is unique, so no {@code params} is needed.
 */
@Mixin(target = "world.WORLD")
public final class TimingStateMixin {

    @Inject(method = "initBeforePlay", at = At.HEAD)
    public void start(State state) {
        state.set(System.nanoTime());
    }

    @Inject(method = "initBeforePlay", at = At.RETURN)
    public void done(State state) {
        long elapsedNs = System.nanoTime() - (long) state.get();
        System.out.println("[PolyphonExample] world init took " + (elapsedNs / 1_000_000) + " ms");
    }
}
