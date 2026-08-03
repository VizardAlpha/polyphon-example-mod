package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.Mixin;
import vizardalpha.polyphon.api.ModifyReturn;

/**
 * {@code @ModifyReturn}: chaining, part 2 of 2. See {@link ModifyReturnFertilityBoostMixin}.
 *
 * <p>{@code priority = 2000} is higher than the boost's default 1000, so this handler runs LATER and
 * has the last word: it caps whatever the boost produced at 1.25. Two independent mods on the same
 * value compose predictably instead of clobbering each other.
 *
 * <p>Verified against songsofsyx 71.44: {@code game.time.TIME.getFertility()D}.
 */
@Mixin(target = "game.time.TIME", priority = 2000) // priority 2000 -> runs LATER, last word
public final class ModifyReturnFertilityCapMixin {

    @ModifyReturn(method = "getFertility")
    public double capFertility(double original) {
        return Math.min(original, 1.25);
    }
}
