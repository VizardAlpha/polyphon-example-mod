package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.Mixin;
import vizardalpha.polyphon.api.ModifyConstant;

/**
 * {@code @ModifyConstant}: replace a constant literal used inside the target method's body. Every
 * load of a constant of the handler's family (int / long / float / double / String) is intercepted;
 * the value about to be pushed is handed to the handler, which returns the value pushed instead. It
 * is the constant-load analogue of {@code @Redirect}. Shapes: {@code (T original)},
 * {@code (Object self, T original)}, or {@code (Object self, T original, <target args>)}, returning
 * {@code T}: the constant's family. Narrow with {@code value} (one exact literal) and/or {@code ordinal}.
 *
 * <p>Verified against songsofsyx 71.44: {@code game.GAME.update(double, double)} contains a single
 * {@code float 0.0625f} literal (the 1/16 sub-tick fraction). {@code value = "0.0625"} matches it
 * exactly, and the handler's {@code float} type fixes the family. Here we halve it to 0.03125.
 */
@Mixin(target = "game.GAME")
public final class ModifyConstantMixin {

    @ModifyConstant(method = "update", params = {double.class, double.class}, value = "0.0625")
    public float halveSubTick(float original) {
        return original / 2f; // 0.0625f -> 0.03125f
    }
}
