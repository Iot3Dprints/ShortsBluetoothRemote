# YouTube Shorts Bluetooth Remote (Android 10)

An Android Studio Kotlin project targeting Android 10 (API 29). It creates a MediaSession to receive Bluetooth media Next/Previous buttons and uses an AccessibilityService swipe gesture to move through Shorts. It does not play audio itself; the intent is to leave YouTube audio on the connected Bluetooth speaker.

## Build
1. Install Android Studio (a current stable release) and its Android SDK platform/build tools.
2. Extract this folder and choose **Open** in Android Studio, selecting the `ShortsBluetoothRemote` folder.
3. Allow Gradle sync to finish. If prompted, install Android SDK Platform 35 and a compatible JDK (Android Studio's bundled JDK is recommended).
4. Select **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. APK output is normally `app/build/outputs/apk/debug/app-debug.apk`.
6. Copy the APK to the Android 10 tablet and install it. You may need to allow installation from that file manager/browser.

## Use
1. Pair the Bluetooth speaker normally and connect it for audio.
2. Open Shorts Bluetooth Remote and tap **Enable accessibility**. In Android Settings > Accessibility, enable **Shorts Bluetooth Remote**.
3. Return to the app and tap **Start Bluetooth remote**.
4. Open YouTube, enter Shorts, and test the speaker's Next and Previous buttons.
5. Stop the service from the app when you are done.

## Important limitations
- This is a starter project; it has not been compiled or tested on your exact tablet/speaker in this environment.
- Bluetooth remotes differ. Some send Next/Previous media-session commands; others send different events. If your speaker's buttons control tracks in another app, they may not be delivered to this app consistently.
- Android generally routes media buttons to the active media session. Activating this remote session may affect which app responds to media buttons, but should not itself reroute audio. Test YouTube playback and button behavior together.
- The app can swipe the screen whenever its accessibility service is enabled; only enable it if you trust the app. It does not read screen content.
- If buttons are not captured while YouTube is active, a more advanced implementation or a different speaker/remote may be needed.
