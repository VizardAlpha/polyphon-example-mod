package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.InjectAt;
import vizardalpha.polyphon.api.Instruction;
import vizardalpha.polyphon.api.Mixin;
import vizardalpha.polyphon.api.Slice;

/**
 * {@code @InjectAt} (+ {@link Slice}): the fine-grained, per-instruction injection point, run code at a
 * PRECISE instruction inside the body, not at the coarse HEAD / RETURN / THROW phases. The selector is
 * {@code at} (instruction KIND: INVOKE, FIELD_GET, FIELD_PUT, NEW, RETURN, JUMP) + {@code target}
 * (the detail) + {@code ordinal} (which match) + {@code after} (before/after) + an optional
 * {@code slice} bounding where it may match (robust against unrelated edits elsewhere). Observation
 * only, like a plain {@code @Inject}: {@code ()}, {@code (self)}, or {@code (self, <target args>)}.
 *
 * <p>Verified against songsofsyx 71.44: in {@code game.GAME.update(double, double)} we inject right
 * AFTER the call to {@code game/GameSpeed.update(D)D}, with a {@link Slice} bounding the search between
 * the {@code AUDIO.update} call and the first {@code GameSaver.autoSave} call.
 */
@Mixin(target = "game.GAME")
public final class InjectAtSliceMixin {

    @InjectAt(
            method = "update", params = {double.class, double.class},
            at = Instruction.INVOKE, target = "game.GameSpeed.update(double)", after = true,
            slice = @Slice(from = "INVOKE game.audio.AUDIO.update(double)",
                           to = "INVOKE game.save.GameSaver.autoSave(double)", toOrdinal = 0))
    public void afterSpeedComputed(Object self, double a, double b) {
        System.out.println("[PolyphonExample] game speed just recomputed in GAME.update");
    }
}
