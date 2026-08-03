package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.Mixin;
import vizardalpha.polyphon.api.ModifyReturn;

/**
 * {@code @Mixin(wraps = ...)}: chaining, part 3 of 3. Order the chain RELATIVE to a named mixin instead
 * of bidding on {@code priority}.
 *
 * <p>Wrapping means taking the OUTER layer: HEAD before theirs, RETURN after theirs, and on a
 * value-modifying point the last word. This mixin keeps the default {@code priority = 1000} yet sits
 * outside {@link ModifyReturnFertilityCapMixin}, which declares {@code priority = 2000}, because
 * {@code wraps} beats priority. So the chain becomes:
 *
 * <pre>
 * [ this ]  [ FertilityCap (2000) ]  [ FertilityBoost (1000) ]     &lt;- outer to inner
 *
 * 1.0  ->  Boost: *1.5  ->  1.5  ->  Cap: min(.., 1.25)  ->  1.25  ->  this: +0.25  ->  1.5
 * </pre>
 *
 * <p>Note what that demonstrates: the outer layer closes LAST, so it can go past a cap an inner mixin
 * just applied. Being outside is power, not politeness.
 *
 * <p><b>Why not {@code before}/{@code after}?</b> Because an order of execution cannot be stated once
 * for a whole mixin: earlier in the chain means running FIRST at HEAD but LAST at RETURN, so "after"
 * would have to mean opposite things depending on the injection point. {@code wraps} names a position,
 * which holds for all of them at once.
 *
 * <p>A name that is not on the classpath is simply ignored, so {@code wraps} is an ordering wish and
 * NOT a dependency: this is what lets you order yourself against another mod's mixin that the player
 * may or may not have installed. Declaring a cycle (two mixins each wrapping the other) is reported once
 * and falls back to priority order.
 *
 * <p>Verified against songsofsyx 71.44: {@code game.time.TIME.getFertility()D}.
 */
@Mixin(target = "game.time.TIME",
        wraps = {"com.example.polyphonexample.showcase.ModifyReturnFertilityCapMixin"})
public final class WrapsRelativeOrderMixin {

    @ModifyReturn(method = "getFertility")
    public double afterTheCap(double original) {
        return original + 0.25;
    }
}
