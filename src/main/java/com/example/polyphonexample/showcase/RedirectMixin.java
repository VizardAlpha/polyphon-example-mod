package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.Mixin;
import vizardalpha.polyphon.api.Redirect;

/**
 * {@code @Redirect}: replace a specific method CALL inside the target method's body. The matching
 * invocation is swapped for a call to the handler, which receives the call's receiver (instance calls)
 * and arguments and returns what the call would have produced. Shapes: instance call
 * {@code (Object callTarget, <call args>)} (receiver declared {@code Object}); static call
 * {@code (<call args>)}. Implemented with {@code invokedynamic}, so it is zero-overhead once linked.
 *
 * <p>Two selectors are at play, do not confuse them: {@code method}/{@code params} pick the ENCLOSING
 * method ({@code update(double, double)}); {@code target} picks the CALL to replace inside it.
 *
 * <p>{@code target} takes the HUMAN spelling shown here (Java type names; the return type is
 * optional, since Java cannot overload on it) or the raw JVM one ({@code "game/GameSpeed.update(D)D"}),
 * byte-exact from {@code javap -c}. Both stay valid; pick per taste.
 *
 * <p>Verified against songsofsyx 71.44: {@code game.GAME.update} calls {@code game.GameSpeed.update(double)}
 * once. We redirect it to return 0, freezing the game clock while the rest of {@code update} runs.
 */
@Mixin(target = "game.GAME")
public final class RedirectMixin {

    @Redirect(method = "update", params = {double.class, double.class},
            target = "game.GameSpeed.update(double)")
    public double freezeClock(Object gameSpeed, double dt) {
        return 0.0; // pretend GameSpeed.update ran and returned 0 -> time does not advance
    }
}
