<div align="center">

# Glimmer AI

### *Not just an AI assistant — a living companion inside your phone.*
### *ليس مساعدًا ذكيًا فحسب… بل رفيق ينبض بالحياة داخل هاتفك.*

<p>
  <img src="docs/assets/glimmer-flame-mark.png" width="200" alt="Glimmer flame mark — original companion emblem, transparent background" />
</p>

<p>
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="docs/assets/glimmer-flame-definition-dark.png" />
    <img src="docs/assets/glimmer-flame-definition.png" width="420" alt="One little flame. A warmer kind of technology. An open-source Android project exploring a gentle AI companion, with privacy and user control at its heart." />
  </picture>
</p>

[![License: AGPL-3.0-or-later](https://img.shields.io/badge/License-AGPL--3.0--or--later-4FC3F7?style=for-the-badge&logo=gnu&logoColor=white)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android%2015%2B%20%2F%20API%2035-69F0AE?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Status](https://img.shields.io/badge/Status-Experimental%20alpha-FF7043?style=for-the-badge)](#current-status)
[![PRs Welcome](https://img.shields.io/badge/PRs-Welcome-FFD54F?style=for-the-badge)](CONTRIBUTING.md)

**Android 15+ · API 35 · Experimental alpha · AGPL-3.0-or-later**

[Overview](#overview) · [Current Status](#current-status) · [Features](#features) · [Digital Soul](#digital-soul--planned) · [Architecture](#architecture) · [Getting Started](#getting-started) · [GlimmerSoul](#glimmersoul-format--proposal) · [Privacy](#privacy) · [Roadmap](#roadmap) · [Contributing](#contributing) · [License](#license)

</div>

---

## Overview

**Glimmer** is an open-source Android project working toward a privacy-first **Ambient AI Companion Layer**: a companion that feels present without taking over your screen.

Its current visual identity is a **single flame spirit** — a dark, rounded body, bright white eyes, and a flowing yellow-to-orange rim. The original flame mark above is that emblem. Directly beneath it, a short English definition is separate artwork sitting **outside** the flame shape; it is not text inside the mark, not a flame-shaped plaque or card, and not a screenshot of the app.

The local Android integration uses a bundled animated 3D model rather than a rainbow ghost or a collection of separate companions. An earlier draft README showed two companion-state portraits (`glimmer-happy.png` / `glimmer-guardian.png`); those are not the adopted header.

The long-term vision combines expressive presence, local intelligence, user-controlled memory, and helpful device actions. **That vision is not the same as the capabilities implemented today.**

```
Traditional AI assistant              Glimmer direction
──────────────────────────────────    ────────────────────────────────
Waits to be called                    Lives with you, without taking over
Cloud-dependent                       On-device first; network features opt-in
Extracts your data                    User control and local processing as goals
Generic personality                   User-shaped Digital Soul (planned)
A tool                                A companion
```

> A companion, not a replacement for human connection. Privacy and user control are design priorities, not claims of a completed audit.

---

## Current Status

**This is an experimental development build, not a production-ready assistant.**

| Area | Current local status |
|---|---|
| Android baseline | `minSdk = 35`, `targetSdk = 35`, `compileSdk = 35` — Android 15+ |
| License | AGPL-3.0-or-later ([LICENSE](LICENSE), [NOTICE](NOTICE)) |
| Companion rendering | Bundled GLB model integrated through Filament and a transparent `TextureView` rendering path |
| State animation | Idle/listening and fade/idle behavior wired into the current flow; thinking and appearance mappings exist but are not reached by the current controller |
| Existing fallback | Previous Canvas-based character retained for loading and recoverable rendering failures |
| Build verification | A debug APK was built; six `FlameMotionTest` JVM tests passed, as recorded in the local integration notes |
| Device verification | **Not run on a physical Android device or emulator**; transparency, overlay behavior, battery use, and GPU performance remain unverified |
| Full AI assistant | Local LLM, speech intelligence, and autonomous task execution are not demonstrated by this character integration |
| Distribution | Local review build; no store availability or public release is claimed here |

The rendering loop is configured for **up to 30 FPS**. This is a code-level cap, **not a measured frame rate**. The bundled model retains its animation clips, but not every clip has a corresponding app event or a verified user flow.

See [the flame integration notes, in Arabic](docs/FLAME_SPIRIT_INTEGRATION_AR.md) for implementation details and known limitations. Historical design documents may describe earlier characters, lower Android requirements, or future systems; the build configuration is authoritative for current compatibility. The unused `MIN_SDK` and `VERSION_NAME` entries in `gradle.properties` are stale; the app Gradle configuration currently specifies API 35 and `0.1.0-alpha`.

---

## Features

### The Living Flame — Integrated, Device Testing Pending

The current work focuses on **one** recognizable companion and its presence above the Android interface:

- A local 3D flame model loaded from `character/src/main/assets/models/flame-spirit-low.glb`.
- Filament-based rendering with lifecycle-aware resource management.
- Animation selection supports listening, thinking, and presence states. The current app flow reaches idle/listening and fade/idle behavior; thinking has no caller, and appearance is not emitted by the controller.
- “Listening” is currently a visual state, not evidence of microphone capture or speech recognition.
- A retained Canvas fallback; this is not a guarantee of recovery from native GPU or driver crashes.
- Existing overlay and permission-onboarding code, which still requires end-to-end validation on Android.

**One companion, one emblem.** The original flame mark introduces the identity. The English definition under it is separate artwork, outside the flame silhouette. Its warm appearance is an artistic direction, not proof of implemented emotional responses, exact in-app rendering, or tested Android performance.

An earlier draft described a rainbow ghost with named color states (calm/happy/working/alert/danger/sleepy). That palette is **not** the current identity and is **not** claimed as a working in-app state machine.

### Local AI Core — Planned Direction

The intended direction is on-device inference, with explicit user control over any future network-dependent features. Candidate technologies include local model runtimes such as llama.cpp or MLC-LLM, subject to integration and validation.

An earlier draft listed unpublished speed/RAM figures for Llama 3.2 3B, Phi-3 Mini, Gemma-2 2B, and TinyLlama 1.1B, plus “40+ languages” and a sub-2-second response claim. **None of those numbers are measurements from this build.** No model compatibility matrix, tokens-per-second benchmark, language count, response-time guarantee, or minimum-memory claim has been established here. The flame renderer does not itself add a language model, microphone understanding, or a conversation engine.

Draft goals that remain unproven:

- No API key required for core local inference
- Core features usable without a network connection after models are present
- Offline-first fallback on constrained hardware

### Digital Soul — Planned

The proposed **Digital Soul** system would let users shape a companion's personality and carry selected preferences between devices.

A previous draft described this onboarding sketch (illustrative only; not a shipped flow):

```
Step 1  → Companion appears with a default presence
Step 2  → Glimmer introduces itself
Step 3  → Optional look / identity choice — Yes / Later
Step 4  → User-controlled appearance preferences
Step 5  → Over time, selected habits stay on-device under explicit user control
```

Potential capabilities include editable personality traits, portable profiles, and explicit control over memory. Automatic personality learning, avatar creation, encrypted Soul export, community marketplace packs, and CC0 “anyone can publish” claims are **not presented as completed features**.

### Ambient Intelligence — Planned

The design goal is helpful, permission-based context rather than constant interruption. Earlier examples (not verified integrations) included:

```
Calendar state        →  a user-requested meeting reminder
Notification patterns →  a user-requested unread summary
Battery level         →  an optional battery-saver suggestion
Time of day           →  a change in visual presence or tone
```

Context collection, accessibility access, and background behavior require separate implementation, consent design, and Android-device testing. Typing-rhythm “stress detection” is not claimed.

### Natural Language Task Execution — Planned

Possible future actions include alarms, calendar queries, and user-confirmed messaging through appropriate Android APIs. Their availability will depend on permissions, platform restrictions, and explicit user authorization.

An earlier draft listed alarm, SMS, calendar, notification, email-summary, duplicate-photo, and malware-scan commands as if they were wired. The current animation work does **not** establish that Glimmer can send messages, read email, execute arbitrary actions, scan malware, or operate a complete voice assistant.

### Wellbeing and Emotional Awareness — Research Direction

A gentle, non-intrusive companion is part of the vision. Emotional inference, stress detection, typing-pattern analysis, wellbeing interventions, and “presence rather than punishment” remain ideas requiring careful technical and privacy evaluation.

**No emotional-detection accuracy, medical benefit, biometric classification, EU AI Act compliance, or regulatory certification is claimed.** Glimmer is not a medical or mental-health service. No camera-based or biometric classification is presented as implemented.

### System Tools and Open Personality Ecosystem — Planned

Duplicate-file detection, permission auditing, malware scanning, plugins, community Souls, and downloadable skins are potential extensions, not shipped tools established by this local build. No ClamAV integration, published plugin API, `souls/` pack directory, or community marketplace is asserted here.

Earlier Soul-type examples (Arabic cultural, mindfulness, developer, education, gaming) remain design notes only.

---

## Architecture

The current project is a modular Kotlin Android application:

```text
Glimmer/
├── app/                  # Android application, entry UI, permission onboarding
├── core/                 # Shared state, models, and core logic
├── overlay/              # Overlay service, accessibility integration, Compose UI
├── character/            # Flame renderer, animations, and legacy character
│   └── src/main/assets/
│       └── models/
│           └── flame-spirit-low.glb
├── data/                 # Placeholder module; no persistence implementation yet
├── docs/                 # Design, architecture, privacy, and integration notes
│   └── assets/           # README artwork
│       ├── glimmer-flame-mark.png
│       ├── glimmer-flame-definition.png
│       └── glimmer-flame-definition-dark.png
├── gradle/               # Gradle Wrapper configuration
├── gradlew
├── gradlew.bat
├── CONTRIBUTING.md
├── CODE_OF_CONDUCT.md
├── SECURITY.md
├── NOTICE
└── LICENSE
```

A previous draft listed `souls/`, `skins/`, `grant/`, `demo/`, `.github/workflows`, and a ghost-overlay Kotlin tree (`GlimmerView`, `EmotionState`, `LocalAI`, `MalwareScanner`, …). Those paths are **not** in this local tree.

### Current Technology

| Layer | Technology in the local project |
|---|---|
| Language | Kotlin 2.0.20 |
| UI | Jetpack Compose; existing Canvas fallback |
| 3D character | Filament + gltfio 1.56.0; bundled GLB |
| Android window layer | Overlay foreground service and Android window APIs |
| Dependency injection | Hilt |
| Build | Android Gradle Plugin 8.6.1 · Gradle Wrapper 8.7 |
| SDK | Android SDK 35 · Java 17 compilation target |

Candidate / not integrated in this character work: llama.cpp, MLC-LLM, ONNX Runtime, Whisper.cpp, TensorFlow Lite emotion models, Room + AES-256 Soul storage, WorkManager task pipelines, ClamAV.

See [Architecture](docs/ARCHITECTURE.md), [Project Structure](docs/PROJECT_STRUCTURE.md), and [Light Being design](docs/LIGHT_BEING.md). These documents include design intent beyond the implemented alpha.

---

## Getting Started

### Requirements

- Android Studio with support for Android Gradle Plugin 8.6.1.
- Android SDK Platform 35 and the required Android build tools.
- JDK 17 or a compatible configured Gradle JDK.
- An Android 15+ / API 35+ device or emulator for runtime testing. Earlier drafts said Android 9+ / SDK 34; that is **not** the current baseline.
- Internet access for the first Gradle dependency download; the bundled flame model itself does not require a network download.

### Open and Build Without a Terminal

1. Open the local project folder in **Android Studio → File → Open**.
2. Select the configured Gradle JDK and allow Gradle sync to finish.
3. Install SDK 35 if Android Studio requests it.
4. Select the `app` module and build a debug APK, or use **Run** with an Android 15+ test device.
5. Follow the app's permission-onboarding flow. Review each permission before enabling the companion overlay or accessibility integration.

Optional command-line equivalents, from the project root:

```bash
./gradlew :character:testDebugUnitTest :app:assembleDebug
# Windows:
gradlew.bat :character:testDebugUnitTest :app:assembleDebug
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

This local snapshot is **not** cloned from `github.com/glimmer-ai/glimmer`. Use the local project folder.

### First Launch — Still Unverified on Device

A previous draft described granting `SYSTEM_ALERT_WINDOW`, tapping **Summon Glimmer**, then downloading **TinyLlama 1.1B Q4**. Overlay permission onboarding exists in code and still needs end-to-end Android testing. No first-run model download is demonstrated by this flame integration.

### Before Calling the Build Usable

Verify the transparent surface over another app, eye visibility, touch behavior, appearance/disappearance, screen locking, repeated start/stop cycles, and the Canvas fallback. Measure memory, battery, heat, and frame pacing on actual hardware. Release shrinking, production signing, and store readiness need separate validation.

**A successful build and JVM tests do not validate those runtime behaviors.**

---

## GlimmerSoul Format — Proposal

A portable Soul format is a design proposal, not a stable public API. The following mixes a previous draft schema with the current flame identity; it is **not** a confirmed importable file in this build:

```json
{
  "schemaVersion": "draft",
  "name": "My Glimmer",
  "author": "optional",
  "language": "ar",
  "avatar": {
    "type": "flame-spirit",
    "colorPrimary": "#FF8A00",
    "accessories": []
  },
  "personality": {
    "humor": 0.8,
    "warmth": 0.9,
    "energy": 0.7,
    "formality": 0.3
  },
  "responses": {
    "greeting": ["Hey!", "Hello there!"],
    "taskDone": ["Done!", "All set!"],
    "error": ["Let me try again."]
  }
}
```

Validation rules, compatibility, import/export, storage security, and a separate format license must be defined before publishing interoperable packs. No CC0 dedication, `souls/` directory, or `docs/GLIMMERSOUL_FORMAT.md` is established by this README. Earlier “publish via PR into `souls/` under CC BY-SA” instructions do not apply until those files exist.

---

## Privacy

The project aims for local processing, meaningful consent, and user-controlled data. These remain engineering responsibilities, not blanket guarantees that “zero data leaves the device.”

An earlier draft listed architectural guarantees (no `INTERNET` permission, AES-256 Soul encryption, reproducible builds, EU AI Act compliance, opt-in analytics). **Those are not proven by this local snapshot.**

The app manifest declares permissions including overlay access, foreground services, microphone recording, notifications, boot reception, and a battery-optimization exemption request. It also registers an accessibility service. **A declared permission is not proof of a working feature, and permission-sensitive code deserves careful review.**

The flame model is bundled locally. This does not, by itself, prove application-wide offline operation, encrypted storage, absence of telemetry, reproducible releases, or legal compliance. Review the final merged manifest, dependencies, data flows, and device behavior before making those claims.

Design and policy references: [Privacy](docs/PRIVACY.md), [Data Governance](docs/DATA_GOVERNANCE.md), [User Rights](docs/USER_RIGHTS.md), [Transparency](docs/TRANSPARENCY.md), and [Security](SECURITY.md). Their presence is not evidence of an independent security audit or regulatory certification.

---

## Intended Contrast — Not a Feature Audit

A previous draft compared Glimmer with Google Assistant, Replika, Character.AI, and Fuzozo using completed-feature checkmarks. That table overstated this alpha. The useful remainder is **direction**, not a scoreboard:

| Theme | Typical cloud assistant | Glimmer direction | This local build |
|---|---|---|---|
| License | Proprietary | Open source | AGPL-3.0-or-later source is present |
| Inference | Cloud | On-device first | Not demonstrated |
| Presence | Voice/chat UI | One flame companion | 3D flame integrated; device testing pending |
| Identity | Generic or cloud avatars | User-controlled Digital Soul | Proposal only |
| Device actions | Often cloud-backed | Narrow, user-confirmed Android APIs | Not demonstrated |
| Distribution | App stores | Independent / F-Droid as a future option | No listing claimed |
| Hardware floor | Varies | Eventually broader than API 35 | Android 15+ / API 35 now |

---

## Roadmap

These are development priorities, not release dates or delivery promises. An earlier 12-month five-phase schedule (Soul → Mind → Senses → Body → Community / F-Droid / v1.0) is **not** a current commitment.

1. Validate flame rendering, transparency, lifecycle handling, and overlay behavior on Android 15+ devices.
2. Measure battery, memory, thermal behavior, and rendering stability; improve fallback coverage.
3. Integrate and benchmark a local AI runtime and a consent-aware speech pipeline.
4. Define user-controlled memory and a versioned Soul format.
5. Add narrowly scoped, user-confirmed device actions and optional contextual assistance.
6. Review privacy and accessibility behavior, test release builds, and prepare distribution documentation.

See [the broader roadmap](docs/ROADMAP.md) for additional design goals. Older scope and schedules may require reconciliation with this alpha.

---

## Contributing

Contributions are welcome, especially Android runtime testing, rendering stability, accessibility, permission UX, privacy review, and clear documentation.

Read [CONTRIBUTING.md](CONTRIBUTING.md) and the [Code of Conduct](CODE_OF_CONDUCT.md). For security concerns, follow [SECURITY.md](SECURITY.md). Report device model, Android version, reproduction steps, and logs with personal data removed when describing a runtime issue.

Useful local starting points (only paths that exist here):

- Runtime and rendering issues → describe them against this alpha; do not assume GitHub issue templates until `.github/` is present
- Documentation → `docs/`
- Conduct and security process → `CODE_OF_CONDUCT.md`, `SECURITY.md`

Earlier draft contribution types (Soul packs, plugin API, translation file maps, ktlint gate, “no telemetry patches”) are not established by this tree.

---

## Funding and Distribution

No awarded grant, submitted funding application, funding amount, F-Droid listing, Play Store availability, or public release is established by this local README review.

An earlier draft named an **NGI Zero Commons Fund** / [NLnet](https://nlnet.nl/commonsfund) application (Thirteenth Call, 2026) with a €35,000 / 12-month figure and F-Droid plus GitHub Releases distribution. There is no `grant/` directory in this snapshot, so that draft is **not** treated as a submitted or awarded application. Funding proposals and distribution plans should be documented and verified separately before badges or claims are added.

---

## License

The repository's [LICENSE](LICENSE) identifies the application as **GNU Affero General Public License, version 3 or later (AGPL-3.0-or-later)**. See also [NOTICE](NOTICE). An earlier draft said GPL-3.0 and assigned CC0 / CC BY-SA / CC BY to Soul spec, assets, and docs; those extra licenses are **not** established here.

This README does not change the project's license or assign separate Creative Commons terms to the Soul proposal, generated illustrations, documentation, or third-party components. Review applicable notices and asset provenance before redistributing.

---

<div align="center">

**Glimmer — Your phone, alive.**  
*An open-source companion in development. Built with user control in mind.*

Created by [ALMLK1996](https://github.com/ALMLK1996)

</div>

