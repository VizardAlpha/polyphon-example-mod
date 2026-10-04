<div align="center">

<img src="Polyphon128x128.ico" width="128" height="128" alt="Polyphon">

# PolyphonExample

**A minimal Songs of Syx mod built on [Polyphon](https://polyphon-docs.vercel.app),** the shared
mixin engine that lets any number of mods hook the same game method without conflicting.

### [Steam Workshop](https://steamcommunity.com/sharedfiles/filedetails/?id=3777203913) · [Documentation](https://polyphon-docs.vercel.app) · [Discord](https://discord.gg/6n4EHkefVu)

</div>

---

A tiny, complete example of a content mod. It injects one line at the start of the main-menu update
loop and prints a greeting, just enough to prove the pipeline works end to end. Copy the folder,
rename it, replace the mixin with your own.

**The key idea:** this mod does **not** bundle Polyphon. It compiles against the Polyphon API with
`provided` scope; at runtime the engine is supplied by the Polyphon mod the player already enabled.
Your jar ships only your `@Mixin` classes plus `META-INF/polyphon.json`.

## What's inside

```
polyphon-example-mod/
├── pom.xml                                   # build: compiles mixins, assembles the mod folder
├── src/main/java/com/example/polyphonexample/
│   ├── ExampleMod.java                       # your logic (no game / Polyphon dependency)
│   ├── mixin/MenuUpdateMixin.java            # the active demo: the one hook that runs in-game
│   └── showcase/                             # one heavily-commented mixin per annotation
└── src/main/resources/
    ├── META-INF/polyphon.json                # lists the mixins Polyphon should apply
    └── mod-files/_Info.txt                   # Songs of Syx mod metadata (filtered at build time)
```

`mixin/` holds the one hook that actually runs. `showcase/` is a **reference**: every Polyphon
annotation, shown once, compile-checked, pointed at **real methods verified against songsofsyx
71.44**. Those classes are *not* listed in `polyphon.json`, so they do nothing until you add one.

## Prerequisites

1. **JDK 21** and **Maven**.
2. **Polyphon in your local Maven repository**, as `io.github.vizardalpha:polyphon-api`.
3. To run it in-game: **Songs of Syx** with [**Polyphon**](https://steamcommunity.com/sharedfiles/filedetails/?id=3777203913) installed and **enabled** in the
   launcher. See [Install](https://polyphon-docs.vercel.app/guide/install).

## Build and install

```bash
mvn package      # produces target/out/PolyphonExample/ (the ready-to-ship mod folder)
mvn install      # also copies it into your Songs of Syx mods directory for local testing
```

Mods directory, if you prefer to copy it yourself:
**Windows** `%AppData%\songsofsyx\mods\` ·
**Linux** `~/.local/share/songsofsyx/mods/` ·
**macOS** `~/Library/Application Support/songsofsyx/mods/`

Then enable **both** `Polyphon` and `PolyphonExample` in the launcher and launch. On the main menu
you'll see, in the game's log:

```
[PolyphonExample] Hello from a Polyphon mixin - the main menu is ticking!
```

## How it works, step by step

1. `MenuUpdateMixin` is `@Mixin(target = "menu.Menu")`; its handler is
   `@Inject(method = "update", params = {float.class, double.class}, at = At.HEAD)`.
2. `META-INF/polyphon.json` lists the mixin so Polyphon knows to apply it.
3. At launch, Polyphon's Java agent reads every `polyphon.json` on the classpath (yours is inside
   `PolyphonExample.jar`), analyses your mixin's bytes with ASM, and records that it targets
   `menu/Menu`.
4. When the game loads `menu.Menu`, the agent transforms `update(float, double)` to call your
   handler at the very start of the method.
5. Your handler calls `ExampleMod.onMenuTick()`, which prints the greeting once, then asks
   `Polyphon` for the engine version and the other mods using it. The mod answers to the `"id"` of
   its `polyphon.json` (`polyphon-example`) when another mod calls `Polyphon.isLoaded`.

Because Polyphon **chains** handlers by priority instead of overwriting methods, other mods can hook
the same method too, without any of you conflicting.

> The same walkthrough, written for a mod you create from scratch, is
> [**Your first mixin**](https://polyphon-docs.vercel.app/guide/first-mixin).

## The showcase

One compile-checked example per annotation, in
[`showcase/`](src/main/java/com/example/polyphonexample/showcase). Every target below was **verified
against songsofsyx 71.44**.

| Example class | Annotation | Verified target (71.44) |
|---|---|---|
| `mixin/MenuUpdateMixin` | [`@Inject(at = HEAD)`](https://polyphon-docs.vercel.app/reference/inject) | `menu.Menu.update(FD)V` |
| `ReturnAndThrowMixin` | [`@Inject`](https://polyphon-docs.vercel.app/reference/inject) at RETURN / TAIL / THROW | `world.WorldGen.load(…)V` |
| `CancellableInjectMixin` | [`@Inject(cancellable = true)`](https://polyphon-docs.vercel.app/helpers/cancellation) | `world.WORLD.IN_BOUNDS(II)Z` |
| `OverwriteMixin` | [`@Overwrite`](https://polyphon-docs.vercel.app/reference/overwrite) (engine 0.2.0+) | `world.WORLD.IN_BOUNDS(II)Z` |
| `TimingStateMixin` | [`State`](https://polyphon-docs.vercel.app/helpers/state) parameter | `world.WORLD.initBeforePlay()V` |
| `ModifyReturnFertility*` | [`@ModifyReturn`](https://polyphon-docs.vercel.app/reference/modify-return) | `game.time.TIME.getFertility()D` |
| `ModifyArgsMixin` | [`@ModifyArgs`](https://polyphon-docs.vercel.app/reference/modify-args) | `game.time.TIME.set(D)V` |
| `ModifyVariableMixin` | [`@ModifyVariable`](https://polyphon-docs.vercel.app/reference/modify-variable) | `game.GAME.update(DD)V` slot 8 |
| `ModifyConstantMixin` | [`@ModifyConstant`](https://polyphon-docs.vercel.app/reference/modify-constant) | `game.GAME.update(DD)V` `0.0625f` |
| `RedirectMixin` | [`@Redirect`](https://polyphon-docs.vercel.app/reference/redirect) on a call | `game.GAME.update` → `GameSpeed.update(D)D` |
| `FieldRedirectMixin` | [`@Redirect`](https://polyphon-docs.vercel.app/reference/redirect) on a field | `game.GAME.update` → `GameResource.isBattle` |
| `InjectAtSliceMixin` | [`@InjectAt` + `@Slice`](https://polyphon-docs.vercel.app/reference/inject-at) | `game.GAME.update(DD)V` |
| `LocalCaptureMixin` | [`@Local`](https://polyphon-docs.vercel.app/reference/local) at an `@InjectAt` point | `game.GAME.update(DD)V` slot 8 |
| `LocalOnInjectMixin` | [`@Local`](https://polyphon-docs.vercel.app/reference/local) on a plain `@Inject` | `game.GAME.update(DD)V` param slot 1 |
| `InstanceStateMixin` | [`InstanceState`](https://polyphon-docs.vercel.app/helpers/instance-state) | `game.GAME`, one counter per session |
| `ShadowAccessMixin` | [`Shadow`](https://polyphon-docs.vercel.app/helpers/shadow) | `game.GAME.updateI` (private int) |
| `WrapsRelativeOrderMixin` | [`@Mixin(wraps = …)`](https://polyphon-docs.vercel.app/concepts/ordering) | `game.time.TIME.getFertility()D` |
| `MultiTargetMixin` | [`@Mixin(targets = …)`](https://polyphon-docs.vercel.app/reference/mixin) | `menu.Menu` + `view.main.VIEW` |
| `RequireMixin` | [`require = true`](https://polyphon-docs.vercel.app/concepts/diagnostics) | `menu.Menu.update(FD)V` |
| `RawTransformMixin` | [`PolyphonTransform`](https://polyphon-docs.vercel.app/reference/transform) | `game.GAME` |
| `ExampleMod` (shipped, not a mixin) | [`Polyphon`](https://polyphon-docs.vercel.app/helpers/polyphon) (engine 0.3.0+) | runtime query: engine version, loaded mods, optional integration |

### Trying one in-game

JSON has no comments, so the showcase classes are simply left off the list rather than commented
out. To run one, add its **full** name to `mixins` in `META-INF/polyphon.json` (they live in a
different package, so no short form), **one at a time**:

```json
{
  "package": "com.example.polyphonexample.mixin",
  "mixins": [
    "MenuUpdateMixin",
    "com.example.polyphonexample.showcase.RedirectMixin"
  ]
}
```

See [`polyphon.json`](https://polyphon-docs.vercel.app/config/polyphon-json) for the side lists and
the engine requirement.

## A note on game versions

> ⚠️ **Targets are game-version specific.** The examples are verified for **songsofsyx 71.44**. Class
> names, method names, descriptors, and especially body-dependent details (a variable slot, a
> constant literal, an instruction ordinal) can change between builds. Re-check with `javap -c` after
> a game update.
>
> A mixin whose target no longer matches is logged and skipped. It never crashes the game. See
> [When it does not apply](https://polyphon-docs.vercel.app/concepts/diagnostics) for how to read
> those warnings.

## Documentation

| | |
|---|---|
| [What Polyphon is](https://polyphon-docs.vercel.app/guide/what-is-polyphon) | the conflict it removes |
| [Install](https://polyphon-docs.vercel.app/guide/install) | for players and for authors |
| [Your first mixin](https://polyphon-docs.vercel.app/guide/first-mixin) | an empty folder to a line in the log |
| [Choosing a target](https://polyphon-docs.vercel.app/concepts/targeting) | classes, methods, overloads |
| [Handler shapes](https://polyphon-docs.vercel.app/concepts/handlers) | what your method may take |
| [Chaining and order](https://polyphon-docs.vercel.app/concepts/ordering) | when two mods want the same method |
| [When it does not apply](https://polyphon-docs.vercel.app/concepts/diagnostics) | reading the log after a game update |

## Getting help

A target that moved after a game update is the usual reason a mixin stops firing, and the log names
what is missing. If the line does not tell you enough, bring it to the
[Discord](https://discord.gg/6n4EHkefVu) with the game version: body-dependent anchors break for
everyone at once, so someone has often already re-mapped it.
