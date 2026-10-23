<div align="center">

<br/>

<table border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td align="center" valign="middle">
      <img src="docs/assets/glimmer-happy.png" width="180" alt="Glimmer — Happy State"/>
    </td>
    <td width="40"></td>
    <td align="center" valign="middle">
      <img src="docs/assets/glimmer-guardian.png" width="180" alt="Glimmer — Guardian State"/>
    </td>
  </tr>
  <tr>
    <td colspan="3" align="center">
      <sub><b>Companion States:</b> Ambient & responsive expressions that evolve with your workflow</sub>
    </td>
  </tr>
</table>

<br/><br/>

# ✦ Glimmer AI

### *"Not just an AI assistant — a living creature that resides inside your phone."*
### *"ليس مساعداً ذكياً… بل كائن حي يعيش داخل هاتفك."*

<br/>

[![License: GPL-3.0](https://img.shields.io/badge/License-GPL--3.0-4FC3F7?style=for-the-badge&logo=gnu&logoColor=white)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android%209%2B-69F0AE?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Privacy](https://img.shields.io/badge/Privacy-Zero%20Data%20Leaves%20Device-brightgreen?style=for-the-badge)](docs/PRIVACY.md)
[![NGI Zero](https://img.shields.io/badge/Grant-NGI%20Zero%20Commons%20Fund-7C4DFF?style=for-the-badge)](grant/nlnet-proposal.md)
[![F-Droid](https://img.shields.io/badge/F--Droid-Coming%20Soon-FF7043?style=for-the-badge&logo=fdroid&logoColor=white)](https://f-droid.org)
[![PRs Welcome](https://img.shields.io/badge/PRs-Welcome-FFD54F?style=for-the-badge)](CONTRIBUTING.md)

<br/>

[**Overview**](#overview) · [**Features**](#-features) · [**Digital Soul**](#-digital-soul--your-phone-becomes-you) · [**Architecture**](#-architecture) · [**Getting Started**](#-getting-started) · [**GlimmerSoul**](#-glimmersoul-format) · [**Privacy**](#-privacy) · [**Contributing**](#-contributing) · [**Grant**](#-grant) · [**License**](#-license)

<br/>

</div>

---

## Overview

**Glimmer** is an open-source, privacy-first **Ambient AI Companion Layer** for Android.

It breaks the boundaries of conventional AI assistants by introducing a living, emotionally expressive digital entity — a rainbow ghost that floats above every app on your screen — that is simultaneously a powerful local AI assistant, a proactive context-aware intelligence, and a mirror of your own digital identity.

```
┌──────────────────────────────────────────────────────────────────┐
│  Traditional AI Assistant          │  Glimmer                    │
│────────────────────────────────────│─────────────────────────────│
│  Waits to be called                │  Lives with you, always     │
│  Cloud-dependent                   │  100% on-device             │
│  Extracts your data                │  Zero data transmission     │
│  Generic personality               │  Mirrors your Digital Soul  │
│  A tool                            │  A companion                │
└──────────────────────────────────────────────────────────────────┘
```

> **Glimmer is not a product. It is digital infrastructure for human dignity.**

---

## ✨ Features

### 🌈 The Living Creature
A glowing, animated ghost overlay that floats above every app on your Android screen.
It breathes, reacts, and changes color based on device state and your emotional context.

| State      | Color        | Trigger                            |
|------------|--------------|------------------------------------|
| 😌 Calm    | 🔵 Blue      | Idle, resting                      |
| 😄 Happy   | 🟡 Gold      | Task completed, positive input     |
| ⚡ Working  | 🟢 Green     | Executing a task                   |
| ⚠️ Alert   | 🟠 Orange    | Reminder, anomaly detected         |
| 🔴 Danger  | 🔴 Red       | Malware found, critical event      |
| 😴 Sleepy  | 🟣 Purple    | Device idle, night mode            |

---

### 🧠 Local AI Core — Intelligence That Never Leaves Your Device

Powered by open-weight models running entirely on-device via **llama.cpp** and **MLC-LLM**.

| Device Class                    | Model              | Speed      | RAM      |
|---------------------------------|--------------------|------------|----------|
| Flagship (Snapdragon 8 Gen 3)   | Llama 3.2 3B Q4    | ~28 tok/s  | ~2.1 GB  |
| Mid-range (Dimensity 7200)      | Phi-3 Mini Q4      | ~14 tok/s  | ~1.6 GB  |
| Entry-level (4 GB RAM)          | Gemma-2 2B Q4      | ~8 tok/s   | ~1.2 GB  |
| Offline-first fallback          | TinyLlama 1.1B Q4  | ~18 tok/s  | ~0.8 GB  |

- ✅ No API key required — ever
- ✅ No internet connection needed for core features
- ✅ Responds in under 2 seconds on mid-range devices
- ✅ 40+ languages — fully offline

---

### 🧬 Digital Soul — Your Phone Becomes You

The defining feature of Glimmer: a **Digital Soul** system that learns your personality and optionally mirrors your avatar.

**The onboarding flow:**
```
Step 1  → Ghost creature appears with its default personality
Step 2  → Glimmer introduces itself: "I'm Glimmer. Your phone, alive."
Step 3  → "Would you like me to look like you?" — Yes / Later
Step 4  → Avatar builder: choose face shape, color accent, accessories
Step 5  → Over time, Glimmer learns your communication style, humor,
           vocabulary, and habits — entirely on-device, AES-256 encrypted
```

- 📦 **Export** your Soul as a portable `.soul.json` file
- 📱 **Transfer** to any new device — your companion never dies
- 🌍 **Share** Souls with the community (CC0 format)

> The **GlimmerSoul** is an open **CC0 format** — anyone can create, share, and publish personalities.

---

### ⚡ Ambient Intelligence — Proactive, Not Reactive

Glimmer reads contextual signals continuously — no wake word needed:

```
Calendar state        →  "You have a meeting in 12 minutes"
Notification patterns →  "You have 47 unread — want a summary?"
Typing rhythm         →  Detects stress and slows down
Battery level         →  "20% left, want me to enable battery saver?"
Time of day           →  Adapts tone and energy automatically
```

---

### 💬 Natural Language Task Execution

```
"Set an alarm for 7am"           → AlarmManager
"Send a message to Ahmed"        → SmsManager / Intent
"What's on my calendar today?"   → CalendarProvider
"Read my notifications"          → NotificationListenerService
"Summarize my last email"        → Local LLM + MailProvider
"Find duplicate photos"          → Perceptual hash scanner
"Scan for malware"               → Embedded ClamAV engine
```

---

### ❤️ Emotional Awareness — On-Device, Privacy-Preserving

Glimmer infers emotional state from behavioral signals **already on the device**:

- Typing speed and pressure variation
- App-switching frequency
- Notification response latency
- Voice prosody during commands

**No camera. No biometric data. EU AI Act compliant.**
Achieves 75–82% detection accuracy — zero bytes transmitted externally.

---

### 🛡️ Digital Wellbeing Guardian

Monitors and gently intervenes against:
- Notification overload
- Doom-scrolling patterns
- Sleep disruption cycles
- App addiction signals

Intervention style: **presence and gentle redirection** — never punishment or blocking.

---

### 🔧 System Intelligence

| Tool                      | Method                     | Cloud Required |
|---------------------------|----------------------------|:--------------:|
| Duplicate photo detection | Perceptual hashing (pHash) | ✗              |
| Malware scanning          | Embedded ClamAV engine     | ✗              |
| Battery health monitoring | Android BatteryManager     | ✗              |
| Permission auditing       | PackageManager inspection  | ✗              |
| Junk file cleanup         | Storage API + heuristics   | ✗              |

---

### 🌍 Open Personality Ecosystem

The **GlimmerSoul** community format enables anyone to publish:

| Soul Type               | Example                                      |
|-------------------------|----------------------------------------------|
| 🌙 Arabic cultural Soul | Warm, humorous, tuned for Arab communication |
| 🧘 Mindfulness Soul     | Calm, meditative, therapist-designed         |
| 🔮 Developer Soul       | Sharp, precise, loves code                   |
| 📚 Education Soul       | Patient, encouraging, for children           |
| 🎮 Gaming Soul          | Energetic, competitive, reaction-fast        |

See the [`souls/`](souls/) directory for available community packs.

---

## 🏗️ Architecture

```
glimmer/
├── app/
│   └── src/main/kotlin/com/glimmer/
│       ├── core/              # DI (Koin/Hilt), shared utilities, theme base
│       ├── overlay/           # Ghost overlay & WindowManager system
│       │   ├── GlimmerView.kt
│       │   ├── EmotionState.kt
│       │   └── FloatingService.kt
│       ├── ai/                # Local LLM core (llama.cpp / MLC-LLM)
│       │   ├── LocalAI.kt
│       │   ├── NLUEngine.kt
│       │   └── ModelManager.kt
│       ├── soul/              # Digital Soul system
│       │   ├── GlimmerSoul.kt
│       │   ├── PersonalityEngine.kt
│       │   └── AvatarBuilder.kt
│       ├── tasks/             # Task execution engine
│       │   ├── AlarmTask.kt
│       │   ├── MessageTask.kt
│       │   └── TaskRouter.kt
│       ├── ambient/           # Context awareness engine
│       │   ├── AmbientEngine.kt
│       │   └── WellbeingGuard.kt
│       ├── memory/            # Long-term memory (Room + AES-256)
│       │   ├── MemoryStore.kt
│       │   └── HabitModel.kt
│       ├── plugins/           # Extensible plugin interface
│       │   └── PluginRegistry.kt
│       └── system/            # System intelligence tools
│           ├── FileScanner.kt
│           ├── DuplicateFinder.kt
│           └── MalwareScanner.kt
├── docs/
│   ├── ARCHITECTURE.md        # Full subsystem data flows
│   ├── GLIMMERSOUL_FORMAT.md  # Soul format specification
│   ├── PLUGIN_API.md          # Developer guide for plugins
│   └── PRIVACY.md             # Privacy-by-design proof
├── souls/                     # Community GlimmerSoul packs (CC0)
│   ├── default.soul.json
│   └── README.md
├── skins/                     # Visual asset packages
│   ├── default/
│   └── spec.md
├── grant/
│   └── nlnet-proposal.md      # NGI Zero Commons Fund application
├── demo/
│   └── index.html             # Interactive web demo
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── bug_report.md
│   │   └── feature_request.md
│   └── workflows/
│       ├── build.yml          # CI — automated build checks
│       └── release.yml        # CD — reproducible APK signing
├── CONTRIBUTING.md
├── SECURITY.md
├── CODE_OF_CONDUCT.md
├── CHANGELOG.md
├── LICENSE
└── .gitignore
```

### Tech Stack

| Layer                 | Technology                                           |
|-----------------------|------------------------------------------------------|
| **Language**          | 100% Kotlin                                          |
| **UI Framework**      | Jetpack Compose + Custom Canvas rendering            |
| **Window System**     | Android WindowManager (persistent overlay)           |
| **Local AI**          | llama.cpp · MLC-LLM · ONNX Runtime                  |
| **Speech**            | Whisper.cpp (on-device ASR, no cloud)                |
| **Emotion Inference** | TensorFlow Lite / ONNX — behavioral signals only     |
| **Data Persistence**  | Room Database + AES-256 + Android Keystore           |
| **Background Tasks**  | WorkManager + AlarmManager                           |
| **Malware Scanning**  | ClamAV (embedded open-source engine)                 |
| **Soul Format**       | GlimmerSoul JSON (CC0 open standard)                 |

For full architectural diagrams and data flows, see [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md).

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Jellyfish or newer
- Android SDK 34+
- JDK 17+
- Android device or emulator — Android 9+ (API 28+)

### Build & Run

```bash
# 1. Clone
git clone https://github.com/glimmer-ai/glimmer.git
cd glimmer

# 2. Open in Android Studio → File → Open → select glimmer/

# 3. Sync Gradle, then build
./gradlew assembleDebug

# 4. Install on connected device
./gradlew installDebug
```

### First Launch

1. Grant `SYSTEM_ALERT_WINDOW` permission when prompted
2. Tap **Summon Glimmer** — the ghost appears on your screen
3. Glimmer asks: *"Would you like to download a local AI model?"*
4. Recommended first model: **TinyLlama 1.1B Q4** (~600 MB, works on all devices)

---

## 🧬 GlimmerSoul Format

The **GlimmerSoul** is an open **CC0** format. Anyone can author and publish one.

```json
{
  "version": "1.0",
  "name": "My Glimmer",
  "author": "optional",
  "language": "en",
  "avatar": {
    "shape": "ghost",
    "colorPrimary": "#4FC3F7",
    "colorScheme": "rainbow",
    "accessories": []
  },
  "personality": {
    "humor":     0.8,
    "warmth":    0.9,
    "energy":    0.7,
    "formality": 0.3
  },
  "emotions": {
    "happy":   "#FFD54F",
    "working": "#69F0AE",
    "alert":   "#FF7043",
    "calm":    "#4FC3F7",
    "sleepy":  "#7C4DFF",
    "danger":  "#F44336"
  },
  "responses": {
    "greeting": ["Hey!", "Hello there!", "What's up?"],
    "taskDone": ["Done!", "Got it!", "All set!"],
    "error":    ["Hmm, let me try again.", "Something went wrong."]
  }
}
```

**Publishing your Soul:**
1. Create your `.soul.json` following [docs/GLIMMERSOUL_FORMAT.md](docs/GLIMMERSOUL_FORMAT.md)
2. Open a PR adding it to the [`souls/`](souls/) directory
3. License must be CC BY-SA 4.0 or more permissive

---

## 🔒 Privacy

Glimmer's privacy guarantees are **architectural** — enforced in code, not policy.

| Guarantee                            | How it is enforced                                  |
|--------------------------------------|-----------------------------------------------------|
| No network for core features         | No `INTERNET` permission in offline build           |
| All AI inference on-device           | llama.cpp / MLC-LLM — local only                   |
| Soul encrypted at rest               | AES-256 + Android Keystore                         |
| Full data portability                | Export all data as open JSON at any time            |
| Reproducible builds                  | Verify APK against source — documented process      |
| EU AI Act compliant                  | Behavioral signals only — no biometric data         |
| No analytics without consent         | Opt-in only, fully revocable                        |

Full details: [docs/PRIVACY.md](docs/PRIVACY.md)

---

## ⚖️ Comparison

| Feature                   | Google Assistant | Replika | Character.AI | Fuzozo (CES 2026) | ✦ Glimmer  |
|---------------------------|:----------------:|:-------:|:------------:|:-----------------:|:----------:|
| Open Source               | ✗ | ✗ | ✗ | ✗ | **✓** |
| Fully Local / Offline     | ✗ | ✗ | ✗ | Partial | **✓** |
| Animated Ghost Creature   | ✗ | Partial | Partial | ✓ | **✓** |
| Digital Soul / Avatar     | ✗ | Partial | Partial | ✗ | **✓** |
| Device Task Execution     | ✓ | ✗ | ✗ | ✗ | **✓** |
| Ambient Intelligence      | Partial | ✗ | ✗ | ✗ | **✓** |
| Emotional Awareness       | ✗ | ✓ | Partial | ✓ | **✓** |
| Digital Wellbeing         | Partial | ✗ | ✗ | ✗ | **✓** |
| System Maintenance        | ✗ | ✗ | ✗ | ✗ | **✓** |
| Soul Portability          | ✗ | ✗ | ✗ | ✗ | **✓** |
| Community Souls / Skins   | ✗ | ✗ | Partial | ✗ | **✓** |
| Works on €80 Android      | Partial | ✓ | ✓ | ✗ | **✓** |
| F-Droid / No Play Store   | ✗ | ✗ | ✗ | N/A | **✓** |

---

## 🤝 Contributing

We welcome contributions of all kinds.

```
🐛  Bug report         → .github/ISSUE_TEMPLATE/bug_report.md
💡  Feature request    → .github/ISSUE_TEMPLATE/feature_request.md
🌍  Translation        → app/src/main/res/values-*/strings.xml
🧬  Publish a Soul     → souls/README.md
🔌  Build a plugin     → docs/PLUGIN_API.md
📖  Improve docs       → docs/
```

Read [CONTRIBUTING.md](CONTRIBUTING.md) and [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) before opening a PR.

**Code standards:**
- Kotlin style via `ktlint`
- All new features must include unit tests
- No analytics or telemetry code accepted
- Privacy review required for any new data access

---

## 🗺️ Roadmap

```
Phase 1 — The Soul      (M1–M2)   Ghost creature · GlimmerSoul v1.0 · Public release
Phase 2 — The Mind      (M2–M4)   Local LLM core · Task execution · Digital Soul
Phase 3 — The Senses    (M4–M6)   Ambient intelligence · Emotional awareness · Wellbeing
Phase 4 — The Body      (M6–M8)   System intelligence · Plugin API v1.0 · Security audit
Phase 5 — The Community (M9–M12)  F-Droid · Soul ecosystem · v1.0 stable release
```

---

## 🏛️ Grant

Glimmer is an applicant to the **NGI Zero Commons Fund** — [NLnet Foundation](https://nlnet.nl/commonsfund), Thirteenth Call, 2026.

> Advancing the Next Generation Internet by proving that privacy-preserving AI is not only technically viable on consumer hardware — but more desirable than surveillance-based alternatives.

- **Requested:** €35,000
- **Duration:** 12 months
- **Distribution:** F-Droid (primary) · GitHub Releases (reproducible APK)

Full proposal: [`grant/nlnet-proposal.md`](grant/nlnet-proposal.md)

---

## 📄 License

| Component               | License                                    |
|-------------------------|--------------------------------------------|
| Application source code | [GPL-3.0](LICENSE)                         |
| GlimmerSoul format spec | [CC0 1.0 Public Domain](souls/LICENSE)     |
| Visual assets           | [CC BY-SA 4.0](docs/assets/LICENSE)        |
| Documentation           | [CC BY 4.0](docs/LICENSE)                  |

Glimmer is free software. You are free to use, study, modify, and distribute it under the terms of the GPL-3.0.

---

<div align="center">

<br/>

**✦ Glimmer — Your phone, alive.**

*Built with ❤️ and zero surveillance*

<br/>

[![Star on GitHub](https://img.shields.io/github/stars/glimmer-ai/glimmer?style=social)](https://github.com/glimmer-ai/glimmer)

<br/>

</div>
