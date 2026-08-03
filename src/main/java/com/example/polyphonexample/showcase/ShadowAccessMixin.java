package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.At;
import vizardalpha.polyphon.api.Inject;
import vizardalpha.polyphon.api.Mixin;
import vizardalpha.polyphon.api.Shadow;

/**
 * {@code Shadow}: read, write, and call the target's PRIVATE members from a handler, the
 * {@code @Shadow}/{@code @Accessor}/{@code @Invoker} need. Members are found by name (superclasses
 * included), resolved once per class, and cached behind {@code MethodHandle}s; statics work by
 * {@code Class} or by name ({@code Shadow.get("game.GAME", "...")}: no game-jar dependency).
 *
 * <p>A typo does not fail silently: the thrown message lists the fields (or method overloads) that
 * actually exist, so a game update reads as a re-mapping task, exactly like the engine's own
 * "never fires" report. The failure is contained by the dispatcher like any handler failure.
 *
 * <p><b>Verified against songsofsyx 71.44.</b> {@code game.GAME} keeps its update counter in a
 * {@code private int updateI}; no public accessor exists. Read it live from a handler:
 */
@Mixin(target = "game.GAME")
public final class ShadowAccessMixin {

    @Inject(method = "update", params = {double.class, double.class}, at = At.HEAD)
    public void peekPrivateCounter(Object self) {
        int updateI = Shadow.get(self, "updateI"); // private field, no accessor, no reflection here
        if (updateI == 100) {
            System.out.println("[PolyphonExample] GAME.updateI just reached " + updateI);
        }
    }
}
