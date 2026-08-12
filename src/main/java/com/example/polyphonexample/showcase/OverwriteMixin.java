package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.Mixin;
import vizardalpha.polyphon.api.Overwrite;

/**
 * {@code @Overwrite}: declarative method replacement, added in engine 0.2.0.
 *
 * <p>The target's body never runs; the handler's return value is the method's return value. The
 * handler is bound ONCE into the call site (like {@code @Redirect}), so a call carries no per-call
 * protocol at all: no {@code Cancellation} object, no argument boxing, no runtime type check. That
 * makes it the right tool whenever a cancellable {@code @Inject} would cancel UNCONDITIONALLY;
 * compare {@link CancellableInjectMixin}, which hooks the same method but keeps the body available
 * for the case it does not cancel.
 *
 * <p>Handler shapes: {@code (<target args>)} with their real types, or, on an instance target,
 * {@code (self, <target args>)} with self declared as {@code Object} or as the target type. The
 * return type must match the target's exactly; the shape is checked when the class is woven, and a
 * mismatch is reported while the original body stays. Replacement is exclusive: if several mixins
 * overwrite the same method, the highest priority wins and the losers are named in the log. Other
 * annotations keep applying AROUND the replacement (HEAD before it, {@code @ModifyReturn} on its
 * value); only kinds anchored on instructions of the replaced body lose their anchor.
 *
 * <p>Verified against songsofsyx 71.44: {@code world.WORLD.IN_BOUNDS(int, int)} is a static
 * predicate, so the handler takes just the two arguments (no {@code self} exists). Demo effect:
 * the world ends at tile 100 in both axes, unconditionally.
 *
 * <p>To ENABLE this showcase, list it in polyphon.json and declare
 * {@code "requiresEngine": ">=0.2.0"} there: an older engine does not know {@code @Overwrite} and
 * would skip it silently, and that field turns the silence into one clear log line.
 */
@Mixin(target = "world.WORLD")
public final class OverwriteMixin {

    @Overwrite(method = "IN_BOUNDS", params = {int.class, int.class})
    public static boolean shrunkWorld(int tx, int ty) {
        return tx >= 0 && ty >= 0 && tx <= 100 && ty <= 100; // stands in for the whole body
    }
}
