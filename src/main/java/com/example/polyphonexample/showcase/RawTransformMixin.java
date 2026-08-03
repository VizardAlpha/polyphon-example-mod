package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.Mixin;
import vizardalpha.polyphon.api.PolyphonTransform;

/**
 * {@link PolyphonTransform}: the raw {@code byte[]} escape hatch, for changes the annotations cannot
 * express: adding fields/methods/interfaces, arbitrary rewrites. A {@code @Mixin} class that
 * implements this interface rewrites its target's whole class file directly, before the
 * annotation-based injections apply.
 *
 * <p>It runs at TRANSFORM time (premain), so do only pure bytecode work and do not touch host classes
 * from it. It exchanges raw {@code byte[]}, so bring your own bytecode library (ASM, ByteBuddy, the
 * JDK Class-File API…); nothing crosses Polyphon's own shaded ASM.
 *
 * <p>This template is a no-op (returns the bytes unchanged), so enabling it is harmless until you add
 * real rewriting logic. Target {@code game.GAME} is a real class in songsofsyx 71.44.
 */
@Mixin(target = "game.GAME")
public final class RawTransformMixin implements PolyphonTransform {

    @Override
    public byte[] transform(String internalName, byte[] classBytes) {
        // e.g. new ClassReader(classBytes) -> ClassWriter -> add a field/method -> return writer.toByteArray();
        return classBytes; // no-op: leave the class unchanged
    }
}
