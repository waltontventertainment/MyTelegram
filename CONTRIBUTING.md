# Contributing to My Telegram

Thank you for your interest in contributing!

---

## How to Contribute

### 1. Fork the Repository
Click "Fork" on GitHub to create your own copy.

### 2. Clone Your Fork
```bash
git clone https://github.com/your-username/my-telegram.git
cd my-telegram
```

### 3. Setup Local Environment
Create `local.properties` in the root folder:
```
TELEGRAM_APP_ID=your_api_id
TELEGRAM_APP_HASH=your_api_hash
sdk.dir=/path/to/your/android/sdk
```
Add your `google-services.json` inside `TMessagesProj/`

### 4. Make Your Changes
- Follow existing code style
- Add Bangla comments where helpful
- Test on a real device or emulator

### 5. Document Your Changes
Add your changes to `CHANGES.md` with:
- What you changed
- Which files were modified
- Why it was changed

### 6. Submit a Pull Request
- Push to your fork
- Open a Pull Request with a clear description

---

## Important Rules (GPL 2.0)

Since this project uses GPL 2.0 license:
- Any modification you contribute **must also be GPL 2.0**
- You **cannot** make your changes proprietary/closed-source
- Always credit the original Telegram source

---

## Key Files to Know

| File | Purpose |
|------|---------|
| `TMessagesProj/src/main/java/org/telegram/ui/ChatActivity.java` | Main chat screen |
| `TMessagesProj/src/main/java/org/telegram/messenger/SharedConfig.java` | App-wide settings/flags |
| `TMessagesProj/src/main/java/org/telegram/ui/MyTelegramSettingsActivity.java` | Custom settings screen |
| `TMessagesProj/src/main/java/org/telegram/messenger/MessagesController.java` | Message handling logic |
| `TMessagesProj/src/main/assets/neon.attheme` | Custom Neon Dark theme |
| `TMessagesProj/src/main/java/org/telegram/ui/ActionBar/Theme.java` | Theme registration |

---

## Need Help?
Open a GitHub Issue with your question!
