package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.Mixin;
import vizardalpha.polyphon.api.ModifyReturn;

/**
 * {@code @ModifyReturn}: replace the value a method returns. The handler takes the original value
 * and returns the new one (its return type must equal the target's).
 *
 * <p><b>Chaining, part 1 of 2</b>: the whole point of Polyphon. This mixin and
 * {@link ModifyReturnFertilityCapMixin} both modify the SAME return value, as if two mods each buffed
 * fertility. Instead of one silently winning, Polyphon feeds each handler's output into the next.
 * Higher priority runs LATER (last word); this one keeps the default 1000, so it runs FIRST: +50%.
 * Net effect of the pair: {@code min(1.0 * 1.5, 1.25) = 1.25}. Swap the priorities and it flips.
 *
 * <p>Verified against songsofsyx 71.44: {@code game.time.TIME.getFertility()} returns {@code double}
 * (base 1.0). The name is unique, so no {@code params}/{@code descriptor} is needed.
 */
@Mixin(target = "game.time.TIME") // priority 1000 (default) -> runs FIRST
public final class ModifyReturnFertilityBoostMixin {

    @ModifyReturn(method = "getFertility")
    public double boostFertility(double original) {
        return original * 1.5; // +50%
    }
}
