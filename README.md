# Android Solana Blink client proof of concept

An example Android app that provides a Blink client to handle unfurling and rendering of any Blink.

The Blink is rendered in its own bottom sheet, so it can be overlayed over other apps.

## Usage

Trigger an intent using adb:

```
adb -s <emulator> shell am start -W -a android.intent.action.VIEW -d "solana-action://<action-url>" com.example.blinkexampleapp
```
