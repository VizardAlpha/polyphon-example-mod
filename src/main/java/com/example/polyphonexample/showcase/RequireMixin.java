package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.At;
import vizardalpha.polyphon.api.Inject;
import vizardalpha.polyphon.api.Mixin;

/**
 * {@code require = true}: mark an injection LOAD-BEARING. Every injection that applies nowhere is
 * already reported (mixin named, what is missing, what to check); {@code require} escalates that
 * report from a warning to an ERROR ({@code ERROR: REQUIRED ...}), so a log reader, or a player
 * pasting a log, sees "this mod is broken", not noise to scroll past. It never goes further than a
 * log line: a broken mod must not stop the game, so containment stays absolute.
 *
 * <p>Use it on the hooks your mod cannot exist without, and leave it off the nice-to-haves; after a
 * game update the log then separates "broken" from "degraded" by itself.
 *
 * <p><b>Verified against songsofsyx 71.44:</b> {@code menu.Menu.update(float, double)}: while it
 * matches, this mixin is silent; rename the method on a future game build and the launcher log
 * carries the REQUIRED error naming this class.
 */
@Mixin(target = "menu.Menu")
public final class RequireMixin {

    @Inject(method = "update", params = {float.class, double.class}, at = At.HEAD, require = true)
    public void mustBeThere() {
        // The handler is not the point: the escalated miss report is.
    }
}
