package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.Mixin;
import vizardalpha.polyphon.api.ModifyVariable;

/**
 * {@code @ModifyVariable}: transform a local variable at the point it is stored. The store to slot
 * {@link ModifyVariable#index()} is intercepted: the value about to be stored is handed to the
 * handler, which returns the value stored instead. Shapes: {@code (T original)},
 * {@code (Object self, T original)}, or {@code (Object self, T original, <target args>)}, returning
 * {@code T}: the variable's type, read from the handler's own signature (no debug info needed).
 *
 * <p>Verified against songsofsyx 71.44: in {@code game.GAME.update(double, double)} the game-speed
 * multiplier is stored into local slot 8. We intercept that store and clamp it to at most 4.0.
 *
 * <p>Note: a variable's <em>slot index</em> is body-specific, so re-check it ({@code javap -c}) whenever
 * the game version changes.
 */
@Mixin(target = "game.GAME")
public final class ModifyVariableMixin {

    @ModifyVariable(method = "update", params = {double.class, double.class}, index = 8)
    public double clampGameSpeed(double speed) {
        return Math.min(speed, 4.0);
    }
}
