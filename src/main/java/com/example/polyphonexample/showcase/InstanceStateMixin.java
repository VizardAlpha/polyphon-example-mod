package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.At;
import vizardalpha.polyphon.api.Inject;
import vizardalpha.polyphon.api.InstanceState;
import vizardalpha.polyphon.api.Mixin;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@code InstanceState}: attach PER-INSTANCE state to a game object without touching its class, the
 * "I wish I could add a field to the target" need. Declare one {@code static final} per piece of
 * state; it behaves like a field would: one value per instance (identity-keyed, the target's
 * {@code equals} is never consulted), created lazily at most once, reclaimed when the instance is
 * collected (weak), safe from any thread.
 *
 * <p><b>Verified against songsofsyx 71.44.</b> {@code game.GAME} is instantiated per loaded session,
 * so this counter counts the updates of THE CURRENT session: load another save and the new
 * {@code GAME} instance starts at zero while the old one is reclaimed with its state; a plain static
 * field in the mixin could not tell the two apart.
 */
@Mixin(target = "game.GAME")
public final class InstanceStateMixin {

    /** One counter PER GAME INSTANCE, i.e. per loaded session; never a leak (weak keys). */
    private static final InstanceState<AtomicInteger> UPDATES =
            InstanceState.withInitial(AtomicInteger::new);

    @Inject(method = "update", params = {double.class, double.class}, at = At.HEAD)
    public void countThisSession(Object self) {
        int count = UPDATES.get(self).incrementAndGet();
        if (count % 10_000 == 0) {
            System.out.println("[PolyphonExample] this session has updated " + count + " times");
        }
    }
}
