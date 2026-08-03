package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.Mixin;
import vizardalpha.polyphon.api.Redirect;

/**
 * {@code @Redirect} on a FIELD ACCESS: replace a specific field read (or write) inside the targeted
 * method, the way a call redirect replaces an invocation. The {@code target} has no parentheses
 * ({@code "owner.name"} or {@code "owner.name:desc"}), and the HANDLER'S RETURN TYPE picks the
 * direction: value-returning = the read (GETFIELD/GETSTATIC), void = the write (PUTFIELD/PUTSTATIC),
 * so one mixin may redirect both with two handlers. Same exclusivity and clash report as call
 * redirects, same {@code invokedynamic} zero-overhead plumbing.
 *
 * <p><b>Verified against songsofsyx 71.44.</b> {@code game.GAME.update(double, double)} loops over
 * the game resources and reads {@code game.GAME$GameResource.isBattle} (a {@code boolean} field) to
 * decide what to update. Redirecting THAT read, in that method only, makes the update loop treat
 * every resource as out-of-battle; every other reader of the field still sees the real value.
 */
@Mixin(target = "game.GAME")
public final class FieldRedirectMixin {

    /** Read redirect: non-void return = GETFIELD; the owner arrives as Object (the resource). */
    @Redirect(method = "update", params = {double.class, double.class},
            target = "game.GAME$GameResource.isBattle:boolean")
    public boolean neverInBattleHere(Object resource) {
        return false; // the update loop sees "not in battle"; the field itself is untouched
    }
}
