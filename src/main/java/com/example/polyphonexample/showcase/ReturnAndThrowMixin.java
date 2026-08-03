package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.At;
import vizardalpha.polyphon.api.Inject;
import vizardalpha.polyphon.api.Mixin;

/**
 * {@code @Inject} at the "exit" phases (vs {@link At#HEAD}), all three returning {@code void}:
 * {@link At#RETURN} right before EVERY {@code return}, {@link At#TAIL} before the LAST one only, and
 * {@link At#THROW} as the method unwinds (the exception then propagates as usual: the handler observes,
 * it does not swallow).
 *
 * <p>RETURN vs TAIL matters as soon as a method has early exits. RETURN fires on whichever {@code return}
 * runs, so a method that bails out early fires it too; TAIL fires only if the final return is reached,
 * which is the "once, on the way out" case. "Last" is positional, not chronological.
 *
 * <p><b>This is the one example that keeps a raw {@code descriptor}.</b> The target takes a game type
 * ({@code snake2d.util.file.FileGetter}) we don't reference at compile time, so {@code params} can't
 * name it: the JVM descriptor {@code "(Lsnake2d/util/file/FileGetter;)V"} is the fallback. (For
 * primitive/JDK arguments, prefer {@code params}: see the other showcase classes.)
 *
 * <p>Verified against songsofsyx 71.44: {@code world.WorldGen.load(FileGetter)} is declared
 * {@code throws IOException}, so both a normal RETURN and a THROW are genuinely reachable.
 */
@Mixin(target = "world.WorldGen")
public final class ReturnAndThrowMixin {

    @Inject(method = "load", descriptor = "(Lsnake2d/util/file/FileGetter;)V", at = At.RETURN)
    public void afterLoad(Object self) {
        System.out.println("[PolyphonExample] world-gen state loaded successfully");
    }

    @Inject(method = "load", descriptor = "(Lsnake2d/util/file/FileGetter;)V", at = At.TAIL)
    public void afterLoadOnce(Object self) {
        System.out.println("[PolyphonExample] world-gen load reached its final return");
    }

    @Inject(method = "load", descriptor = "(Lsnake2d/util/file/FileGetter;)V", at = At.THROW)
    public void loadThrew(Object self) {
        System.out.println("[PolyphonExample] world-gen load is throwing (the exception still propagates)");
    }
}
