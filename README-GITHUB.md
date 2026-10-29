# Build the APK with GitHub Actions

This project includes `.github/workflows/build-apk.yml`. After you push the project files to a GitHub repository, the workflow builds a debug APK on every push. You can also run it manually from the **Actions** tab using **Build Android APK** → **Run workflow**.

## Upload the project
1. Create a new repository on https://github.com/new.
2. Extract the project ZIP.
3. Open the `ShortsBluetoothRemote` folder and upload its *contents* to the repository root (make sure `.github/workflows/build-apk.yml` is included). You can do this with GitHub's web upload, or use Git from your PC.
4. Open the repository's **Actions** tab and select **Build Android APK**.
5. When the run finishes successfully, open that run and download **ShortsBluetoothRemote-debug-apk** from **Artifacts**.
6. Extract the downloaded artifact ZIP; it contains `app-debug.apk`. Transfer it to the Android 10 tablet and install it.

The workflow produces a debug APK for testing. It does not prove that every speaker sends media-button events in a way the app can use; test with your own speaker. Do not upload signing keys or passwords to the repository.
