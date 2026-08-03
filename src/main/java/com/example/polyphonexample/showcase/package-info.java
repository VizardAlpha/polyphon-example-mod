/**
 * Reference showcase: one small, heavily-commented mixin per Polyphon annotation.
 *
 * <p>These classes exist to show the <em>syntax and handler contract</em> of every injection kind.
 * They compile against the Polyphon API (so the shapes are guaranteed correct), and every target was
 * <strong>verified against songsofsyx 71.44</strong>: real classes, methods and (for body-based ones)
 * bytecode offsets.
 *
 * <p>They are <em>not</em> listed in {@code META-INF/polyphon.json}, so the shipped mod only runs the
 * harmless menu greeting. To try one in-game, add its fully-qualified name to the {@code mixins} list
 * (see the README), <em>one at a time</em>. Body-dependent details (a variable slot, a constant
 * literal, an instruction ordinal) are version-specific; a mixin whose target no longer matches is
 * logged and skipped, never crashing the game.
 *
 * <p><b>Chaining</b>, the reason Polyphon exists, is shown by three mixins on the SAME return value,
 * which Polyphon runs as a chain instead of letting one overwrite the others:
 * {@link com.example.polyphonexample.showcase.ModifyReturnFertilityBoostMixin} and
 * {@link com.example.polyphonexample.showcase.ModifyReturnFertilityCapMixin} order themselves with
 * {@code priority}, while {@link com.example.polyphonexample.showcase.WrapsRelativeOrderMixin} shows the
 * relative alternative: {@code wraps} claims the outer layer over a NAMED mixin and beats priority, so
 * independent authors never have to bid numbers against each other.
 */
package com.example.polyphonexample.showcase;
