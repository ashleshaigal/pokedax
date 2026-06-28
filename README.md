# Pokédex App Setup & Development Guide

This Pokédex application is built with Jetpack Compose, Firebase, and follow a high-contrast Pokédex-inspired theme.

## 🚀 Initial Setup

Before you can build and run the project, the following files and configurations must be added manually, as they are excluded from version control for security.

### 1. `local.properties`
Create a `local.properties` file in the root directory (if not present) and add the following properties for signing the release APK/AAB:

```properties
sdk.dir=/path/to/your/android/sdk
signing.storeFile=pokedax.jks
signing.storePassword=YOUR_STORE_PASSWORD
signing.keyAlias=YOUR_KEY_ALIAS
signing.keyPassword=YOUR_KEY_PASSWORD
```

Ensure your keystore file (`pokedax.jks` or similar) is placed in the `app/` directory as configured.

### 2. `google-services.json`
To enable Firebase Authentication (Google Sign-In) and Firestore, you must:
1.  Download the `google-services.json` file from your [Firebase Console](https://console.firebase.google.com/).
2.  Place it in the `app/` directory: `/pokedax/app/google-services.json`.

### 3. Google Sign-In (SHA-1)
For Google Sign-In to function, you **must** register your machine's SHA-1 fingerprint in the Firebase project settings.
*   **Debug Key**: Usually found in `~/.android/debug.keystore`.
*   **Release Key**: The fingerprint of your `pokedax.jks`.

---

## 🛠 Build & Versioning

The project uses a custom versioning system defined in `version.properties`.

### Version Management
*   **`VERSION_CODE`**: Increments **automatically** by +1 every time a `release` build task (e.g., `bundleRelease` or `assembleRelease`) is executed.
*   **`VERSION_NAME`**: Must be updated **manually** in `version.properties`. It is treated as a static string (e.g., `1.6.0`).

### Generating a Build
When generating a signed bundle or APK:
1.  Navigate to **Build > Generate Signed Bundle / APK...**
2.  Follow the prompts using the credentials stored in your `local.properties`.
3.  The output will be generated in `app/release/`. This folder is ignored by Git to keep the repository clean.

---

## 🎨 UI & UX Features

*   **Pokédex Theme**: High-contrast theme featuring Pokédex Red (`#E3350D`) and Electric Blue accents.
*   **Modern Search**: A pill-shaped, modern search bar on the listing screen that automatically hides on scroll-down and reappears on scroll-up to maximize screen space.
*   **Bottom Sheet Navigation**: All major settings actions (Disclaimer, Copyright, Social) use consistent, fully-expanded `ModalBottomSheet` components.
*   **Dynamic Bottom Bar**: The "Saved" tab is hidden in guest mode and automatically appears after a successful Google login.
*   **Edge-to-Edge Splash**: A full-screen splash image with system-matched status and navigation bars.

---

## 📁 Repository Cleanup
If you accidentally track large binary files in the future, remember that the following are currently ignored:
*   `/build` folders
*   `/release` folders
*   `google-services.json`
*   `local.properties`
*   `*.jks` (Keystore files)
