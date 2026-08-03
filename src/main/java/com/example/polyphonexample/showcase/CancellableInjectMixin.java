package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.At;
import vizardalpha.polyphon.api.Cancellation;
import vizardalpha.polyphon.api.Inject;
import vizardalpha.polyphon.api.Mixin;

/**
 * {@code @Inject(cancellable = true)}: a HEAD handler that can short-circuit the target method.
 *
 * <p>With {@code cancellable = true} the handler receives a {@link Cancellation} right after
 * {@code self}: call {@code ci.cancel()} on a void method, or {@code ci.cancel(value)} to skip the
 * body AND force the return value (a mistyped value is logged and ignored, never corrupting the host).
 * Handler shapes: {@code (ci)}, {@code (self, ci)}, or {@code (self, ci, <target args>)}. The first
 * valid cancel wins and stops the chain.
 *
 * <p>Verified against songsofsyx 71.44: {@code world.WORLD.IN_BOUNDS(int, int)} is a static predicate
 * ("is tile (x, y) inside the world?"). It is overloaded, so {@code params = {int.class, int.class}}
 * selects the {@code (int, int)} version. The target is static, so {@code self} is {@code null}; we
 * still declare it to also read the two arguments. Safe demo: we only ever make bounds tighter.
 */
@Mixin(target = "world.WORLD")
public final class CancellableInjectMixin {

    @Inject(method = "IN_BOUNDS", params = {int.class, int.class}, at = At.HEAD, cancellable = true)
    public void shrinkWorld(Object self, Cancellation ci, int tx, int ty) {
        if (tx > 100 || ty > 100) {
            ci.cancel(Boolean.FALSE); // treat far tiles as out of bounds; skip the real check
        }
        // otherwise: no cancel -> the original IN_BOUNDS body runs normally
    }
}
