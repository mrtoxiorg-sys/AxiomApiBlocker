# AxiomApiBlocker

Fabric client-side mod that blocks Axiom's access to its API (`axiom.moulberry.com`) to test how Axiom behaves when its API is unavailable.

Minecraft: **1.21.11** (Fabric). The mod is client-side only.

## What it does

- Mixins into `com.moulberry.axiom.utils.Authorization` and `com.moulberry.axiom.i18n.LocalizationLoader`, intercepting every `URL.openConnection()` call to `axiom.moulberry.com` and failing it with an `IOException`.
- Additional network-level protection via a `ProxySelector` that forces all requests to the Axiom host to fail.
- Every blocked attempt is printed to the in-game chat: `[HH:mm:ss] WARN: ... (всего заблокировано: N)`.

This makes Axiom hit its own error-handling paths:

- `getMeta()` -> `null` (version/changelog checks fail)
- `checkCommercial()` -> `false` (no commercial license)
- `checkServer()` -> `YES` (Axiom deliberately allows servers when the API is unreachable)

## Building

```sh
./gradlew build
```

You need the Axiom jar in `libs/` (`libs/Axiom-5.4.2-for-MC1.21.11.jar`) as a compile-only dependency. It is not required at runtime — the mod just needs Axiom installed in the game.

Output: `build/libs/AxiomApiBlocker-1.21.11-1.0.0.jar`

## Usage

Drop the jar into `mods/` along with Axiom. Watch the chat for blocked requests.

## License

This is a test/educational tool for Axiom's own API. Use responsibly.