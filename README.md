# My Telegram

A customized Telegram Android app fork with exclusive features for Bangladeshi users.

## Based On
This project is a fork of the official [Telegram for Android](https://github.com/DrKLO/Telegram) source code, licensed under [GPL v2.0](https://www.gnu.org/licenses/old-licenses/gpl-2.0.html).

## Custom Features Added
- Ghost Mode — read messages without showing "Seen"
- Anti-Delete — see deleted messages
- Auto Reply — automatic reply per chat
- Custom Chat Background — set personal or group background
- Neon Dark Theme — built-in custom dark theme with theme switcher
- Premium Unlocked — all Telegram Premium features free for all users
- Custom App Icon (Phoenix Neon)

## Setup (for Developers)
1. Clone this repo
2. Create `local.properties` in the root:
```
TELEGRAM_APP_ID=your_app_id
TELEGRAM_APP_HASH=your_app_hash
sdk.dir=/path/to/android/sdk
```
3. Add your `google-services.json` inside `TMessagesProj/`
4. Build with Android Studio or GitHub Actions

## Build via GitHub Actions
Add these secrets to your GitHub repo:
- `TELEGRAM_APP_ID`
- `TELEGRAM_APP_HASH`
- `GOOGLE_SERVICES_JSON` (base64 encoded content of google-services.json)

## License
This project inherits the [GPL v2.0](LICENSE) license from the original Telegram source code.
All custom modifications are also released under GPL v2.0.

## Disclaimer
This is an independent project and is NOT affiliated with or endorsed by Telegram Messenger.
