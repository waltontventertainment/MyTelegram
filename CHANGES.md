# Custom Changes — FeelWire Android App

All modifications made on top of the official Telegram Android source code.

---

## Rebranding (FeelWire)

### App Name & Package
- **App Name:** `FeelWire` (Beta: `FeelWire Beta`)
- **Package:** `com.mytelegram.yourtelegram` (unchanged — custom package)
- **Files:** `strings.xml`, `gradle.properties`
- Login subtitle changed to: "Sign in to FeelWire with your phone number."

### Onyx Dark + Neon Purple Theme
- **File:** `TMessagesProj/src/main/java/org/telegram/ui/ActionBar/ThemeColors.java`
- Full color system overhaul:
  - Background: `#0F0F1A` (Deep Onyx Black)
  - Surface: `#1C1C2E` (Onyx Indigo)
  - Accent: `#8B5CF6` (Neon Purple) / `#7C3AED` (Deep Purple)
  - Incoming bubble: `#252540` | Outgoing bubble: `#4A1FA8`
  - Outgoing metadata (time, checkmarks, views): `#CCB8FF` (Soft Lavender)
  - Input panel text: `#E8E8F0` | Cursor: `#8B5CF6` | Hint: `#6060A0`
  - Dialog backgrounds, menu, all gray surfaces → dark Onyx variants

### Chat Bubble Radius
- **File:** `TMessagesProj/src/main/java/org/telegram/messenger/SharedConfig.java`
- Default bubble radius: `17` → `20` dp (rounder, no-tail premium style)

### Login Screen Branding
- **File:** `TMessagesProj/src/main/java/org/telegram/ui/LoginActivity.java`
- Added bold "FeelWire" brand header (36sp, Neon Purple `#8B5CF6`) above phone entry

### Pill-Shaped Unread Badges
- **File:** `TMessagesProj/src/main/java/org/telegram/ui/Cells/DialogCell.java`
- `BADGE_TEXT_PADDING`: `6.333f` → `8f` (wider pill)
- `BADGE_SIZE`: `20.666f` → `22f` (taller capsule)

### Floating Message Input Bar
- **File:** `TMessagesProj/src/main/java/org/telegram/ui/ChatActivity.java`
- Input bar margins: `7,0,7,0` → `10,0,10,8` (floats off screen edge with bottom gap)

### FeelWire Splash Screen
- **File:** `TMessagesProj/src/main/java/org/telegram/ui/FeelWireSplashFragment.java`
- Animated branded intro on every fresh launch (not logged in):
  - Radial neon glow background fades in
  - Logo mark bounces in with overshoot spring animation
  - "FeelWire" wordmark slides up and fades in
  - "Feel every message." tagline fades in
  - After 900ms hold, fades out and transitions to the standard Intro/Login flow
- **File:** `TMessagesProj/src/main/java/org/telegram/ui/LaunchActivity.java`
  - `getClientNotActivatedFragment()` returns `FeelWireSplashFragment` instead of `IntroActivity`

---

## Features Added

### Ghost Mode
- **File:** `TMessagesProj/src/main/java/org/telegram/messenger/SharedConfig.java`
- **File:** `TMessagesProj/src/main/java/org/telegram/messenger/MessagesController.java`
- Read messages without sending "Seen" status

### Anti-Delete
- **File:** `TMessagesProj/src/main/java/org/telegram/messenger/MessagesController.java`
- Saves deleted messages locally before they are removed

### Auto Reply
- **File:** `TMessagesProj/src/main/java/org/telegram/ui/ChatActivity.java`
- Per-chat auto reply toggle with custom message text

### Custom Chat Background
- **File:** `TMessagesProj/src/main/java/org/telegram/ui/ChatActivity.java`
- Set personal or group/channel background image per chat

### Premium Unlocked
- **File:** `TMessagesProj/src/main/java/org/telegram/messenger/UserConfig.java`
- `isPremium()` returns `true` for all users

### Custom App Icon
- **Files:** `TMessagesProj/src/main/res/mipmap-*/ic_launcher.png`
- Phoenix Neon style icon replacing original Telegram icon

### API Credentials (Secured)
- **File:** `TMessagesProj/src/main/java/org/telegram/messenger/BuildVars.java`
- Reads from `BuildConfig` — secrets stored in `local.properties` and GitHub Secrets

---

## Settings Screen
- **File:** `TMessagesProj/src/main/java/org/telegram/ui/MyTelegramSettingsActivity.java`
- Sections: Theme, Privacy, Security, Auto-Reply

---

## Landing Page
- **File:** `web/index.html`
- Full FeelWire dark-themed landing page (served in Replit preview)
