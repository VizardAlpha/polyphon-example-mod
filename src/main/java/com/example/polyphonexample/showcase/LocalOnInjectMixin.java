package com.example.polyphonexample.showcase;

import vizardalpha.polyphon.api.At;
import vizardalpha.polyphon.api.Inject;
import vizardalpha.polyphon.api.Local;
import vizardalpha.polyphon.api.LocalDoubleRef;
import vizardalpha.polyphon.api.Mixin;

/**
 * {@code @Local} on a PLAIN {@code @Inject}: the capture {@link LocalCaptureMixin} does with an
 * {@code @InjectAt} instruction selector also works at the coarse phases, where it reads naturally:
 * a snapshot right before a RETURN/TAIL, a writable ref at HEAD. At HEAD only the parameter slots
 * (and slots already written) exist yet, so HEAD captures should stick to parameters. Not
 * combinable with {@code cancellable}, a {@code State} parameter, or THROW (each owns its own
 * handler shape; the mix is rejected with a named reason).
 *
 * <p><b>Verified against songsofsyx 71.44.</b> {@code game.GAME.update(double, double)}: the first
 * {@code double} parameter (slot 1, this = 0) is the tick delta. Halving it AT HEAD through a ref
 * is a two-line slow-motion: the body then runs with the halved value. (Compare
 * {@link ModifyArgsMixin}, which rewrites arguments through the boxed array: a ref reads better
 * when one typed parameter is the whole story.)
 */
@Mixin(target = "game.GAME")
public final class LocalOnInjectMixin {

    @Inject(method = "update", params = {double.class, double.class}, at = At.HEAD)
    public void slowMotion(Object self, double ds, double d,
                           @Local(index = 1) LocalDoubleRef delta) {
        delta.set(delta.get() * 0.5); // written back into slot 1; the body sees half the delta
    }
}
