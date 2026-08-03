package com.example.polyphonexample.mixin;

import vizardalpha.polyphon.api.At;
import vizardalpha.polyphon.api.Inject;
import vizardalpha.polyphon.api.Mixin;

import com.example.polyphonexample.ExampleMod;

/**
 * The simplest possible Polyphon hook: run our code at the start of a game method.
 *
 * <ul>
 *   <li>{@code @Mixin(target = "menu.Menu")}: the host class to transform, named as a String so we
 *       need no compile-time dependency on the game jar. If you DO compile against the target, prefer
 *       the class literal: {@code @Mixin(Menu.class)}.</li>
 *   <li>{@code @Inject(at = HEAD)}: call our handler before the original body of {@code Menu.update}.</li>
 *   <li>{@code params = {float.class, double.class}}: picks the {@code update(float, double)} overload.
 *       Readable and IDE-completed; no cryptic descriptor to memorize. (See the README for when a raw
 *       {@code descriptor} is still needed.)</li>
 * </ul>
 *
 * <p>The {@code @Inject} handler returns {@code void} and takes {@code ()}, {@code (self)}, or
 * {@code (self, <target args>)}. Here we need nothing from the game, so {@code ()}.
 */
@Mixin(target = "menu.Menu")
public final class MenuUpdateMixin {

    @Inject(method = "update", params = {float.class, double.class}, at = At.HEAD)
    public void onMenuUpdate() {
        ExampleMod.onMenuTick();
    }
}
