package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.Mixin;
import vizardalpha.polyphon.api.ModifyArgs;

/**
 * {@code @ModifyArgs}: rewrite the arguments the target method receives, before its body runs. The
 * handler gets the arguments boxed into an {@code Object[]} and mutates it in place; each element is
 * written back into the matching parameter. Shapes: {@code (Object[] args)} or
 * {@code (Object self, Object[] args)}, returning {@code void}. A value whose type no longer matches
 * the parameter is reverted and logged, so the host never receives a corrupted argument.
 *
 * <p>Verified against songsofsyx 71.44: {@code game.time.TIME.set(double)} sets the in-game time
 * (seconds). {@code params = {double.class}} pins the overload; we clamp {@code args[0]} to be
 * non-negative before the body stores it.
 */
@Mixin(target = "game.time.TIME")
public final class ModifyArgsMixin {

    @ModifyArgs(method = "set", params = {double.class})
    public void clampTime(Object[] args) {
        args[0] = Math.max(0.0, (Double) args[0]); // never set a negative time
    }
}
