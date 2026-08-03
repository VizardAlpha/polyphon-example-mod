package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.InjectAt;
import vizardalpha.polyphon.api.Instruction;
import vizardalpha.polyphon.api.Local;
import vizardalpha.polyphon.api.LocalDoubleRef;
import vizardalpha.polyphon.api.Mixin;

/**
 * {@code @Local}: read and WRITE a target method's local variables at an {@code @InjectAt} point, via
 * handler PARAMETERS. A plain value parameter is a read-only snapshot; a ref holder
 * ({@code LocalIntRef}, {@code LocalLongRef}, {@code LocalFloatRef}, {@code LocalDoubleRef},
 * {@code LocalBooleanRef}, or {@code LocalRef<T>}) reads the slot and writes whatever it holds back
 * into it after the handler returns.
 *
 * <p>Select the local three ways (no game-jar dependency needed, since {@code @Local} uses JDK types and
 * the ref holders):
 * <pre>
 * &#64;Local(index = 8)                        LocalDoubleRef speed  // by absolute slot (works even without debug info)
 * &#64;Local(name = "speed")                   LocalDoubleRef speed  // by name, from the LocalVariableTable
 * &#64;Local(type = double.class, ordinal = 0) LocalDoubleRef speed  // by the Nth local of a type, from the LVT
 * </pre>
 *
 * <p><b>Verified against songsofsyx 71.44.</b> In {@code game.GAME.update(double, double)} the
 * game-speed multiplier is a {@code double} local in slot 8 (see {@link ModifyVariableMixin}). At the
 * {@code @InjectAt} point on {@code GameSpeed.update}, we take a read-only snapshot of that local and,
 * via a ref and clamps it, writing the clamped value back into the slot.
 *
 * <p>An unresolvable {@code @Local} (a name absent from the debug info, an out-of-range ordinal, a
 * mismatched type) disables just this handler, logged, never the game. Slot indices are body-specific;
 * re-check with {@code javap -c} on a game update, or prefer {@code name} / {@code type} selection.
 */
@Mixin(target = "game.GAME")
public final class LocalCaptureMixin {

    @InjectAt(method = "update", params = {double.class, double.class},
            at = Instruction.INVOKE, target = "game/GameSpeed.update(D)D", after = true)
    public void clampGameSpeed(Object self, double a, double b,
                               @Local(index = 8) double speedSnapshot,
                               @Local(index = 8) LocalDoubleRef speed) {
        if (speed.get() > 4.0) {
            speed.set(4.0); // written back into slot 8; speedSnapshot keeps the pre-clamp value
        }
    }
}
