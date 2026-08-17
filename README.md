# AxiomApiBlocker

Fabric client-side mod that blocks Axiom's access to its API (`axiom.moulberry.com`) and demonstrates that the license/whitelist enforcement in Axiom (v5.4.2, MC 1.21.11) can be bypassed on any server where you have OP rights, without a commercial license and restrictions.

## What it does

The mod ships three mixins and a network-level `ProxySelector`:

### 1. Network blocking

- `AuthorizationMixin` — mixins into the authorization classes, intercepting every `URL.openConnection()` call to `axiom.moulberry.com` and failing it with an `IOException`.
- `ProxySelector` — forces all requests to the Axiom host to fail at the socket level, as a second layer of defense.

Every blocked attempt is printed to the in-game chat: `[HH:mm:ss] WARN: ... (всего заблокировано: N)`.

### 2. License forcing

- `AuthorizationLicenseMixin` — forces `Authorization.hasCommercialLicense()` to always return `true`.

### 3. Server whitelist bypass

- `AuthorizationServerMixin` — short-circuits `Authorization.checkServer()` and immediately returns `ServerAuthorization.YES` (no network request, no JWT verification).

### Why the homoglyph?

The Axiom jar ships a nested jar (`META-INF/jars/axiomclientapi.jar`) whose authorization classes are obfuscated with a **Greek omicron homoglyph** (`com.moulberry.axi\u03BFm.utils.Authorization`, U+03BF instead of U+006F). The main jar's `ClientEvents` references the homoglyph class, so at runtime **all** authorization flows go through the nested jar. The mixins therefore target **both** the Latin and the homoglyph class name, otherwise they silently do not apply.

This explains the error paths seen in the vanilla mod:

- `getMeta()` -> `null` (version/changelog checks fail)
- `checkCommercial()` -> `false` (no commercial license)
- `checkServer()` -> `YES` (older builds of Axiom deliberately allow servers when the API is unreachable — the nested jar instead returns `NO` on error, which is what the server mixin neutralizes)

## Building

```sh
./gradlew build
```

You need the Axiom jar in `libs/` (`libs/Axiom-5.4.2-for-MC1.21.11.jar`) as a compile-only dependency. It is not required at runtime — the mod just needs Axiom installed in the game.

Output: `build/libs/AxiomApiBlocker-1.21.11-1.1.2.jar`

## Usage

Drop the jar into `mods/` along with Axiom. Watch the chat for blocked requests.

## License

This is a test/educational tool demonstrating holes in Axiom's own API. Use responsibly.
