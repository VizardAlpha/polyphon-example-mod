package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.At;
import vizardalpha.polyphon.api.Inject;
import vizardalpha.polyphon.api.Mixin;

/**
 * {@code @Mixin(targets = {...})}: ONE mixin class applied to SEVERAL host classes: the same tweak
 * written once instead of once per class. {@code targets} unions with {@code value}/{@code target};
 * every listed class gets its own chain position, and a miss is reported per target.
 *
 * <p><b>Verified against songsofsyx 71.44.</b> The menu ({@code menu.Menu}) and the in-game view
 * ({@code view.main.VIEW}) are both {@code snake2d.CORE_STATE}s with the same
 * {@code update(float, double)} shape, so one handler observes the tick WHEREVER the player is,
 * with one class. The handler shape {@code (self)} fits both targets; {@code self} tells them apart.
 */
@Mixin(target = "menu.Menu", targets = {"view.main.VIEW"})
public final class MultiTargetMixin {

    private static Class<?> lastState;

    @Inject(method = "update", params = {float.class, double.class}, at = At.HEAD)
    public void onAnyStateTick(Object self) {
        if (self.getClass() != lastState) {
            lastState = self.getClass();
            System.out.println("[PolyphonExample] now ticking in " + lastState.getSimpleName());
        }
    }
}
