# expo-privacy-image

A React Native package that overlays a privacy image on the screen whenever the app goes to the background, preventing sensitive content from appearing in the app switcher (iOS) or recent apps (Android).

Built with [Expo Modules](https://docs.expo.dev/modules/overview/).

<img src="https://raw.githubusercontent.com/ntphu1808/expo-privacy-image/main/assets/preview.png" width="400" alt="Privacy overlay preview" />

---

## Platform support

| Android | iOS |
| ------- | --- |
| ✅      | ✅  |

---

## Installation

#### Bare React Native: 
Ensure [expo-modules](https://docs.expo.dev/bare/installing-expo-modules/) is installed before proceeding, then run the command below.

#### Expo:

```sh
npx expo install expo-privacy-image
```

---

## Setup

> **Image format:** Only **PNG** is supported.

### Expo managed

Add the plugin to your `app.config.ts` (or `app.json`) and point it at your privacy image:

```ts
// app.config.ts
export default {
  plugins: [
    [
      "expo-privacy-image",
      {
        imagePath: "./assets/images/privacy.png",
      },
    ],
  ],
};
```

Then run prebuild to apply the native changes:

```sh
npx expo prebuild
```

The plugin automatically:
- **Android**: copies the image to `android/app/src/main/res/drawable/privacy_image.png`
- **iOS**: adds the image as `PrivacyImage.imageset` inside `Images.xcassets`

### Bare React Native (manual setup)

**Android**

Copy your privacy image to:

```
android/app/src/main/res/drawable/privacy_image.png
```

**iOS**

Create the imageset directory and add your image:

```
ios/<ProjectName>/Images.xcassets/PrivacyImage.imageset/
  privacy_image.png
  Contents.json
```

`Contents.json` should contain:

```json
{
  "images": [
    { 
      "idiom": "universal",
      "filename": "privacy_image.png",
      "scale": "1x"
    }
  ],
  "info": { "version": 1, "author": "xcode" }
}
```

Then run:

```sh
npx pod-install
```

---

## Usage

Call `usePrivacyImage()` once, typically at the root of your app or inside a top-level component. Once called, the module is permanently enabled for the lifetime of the app session.

```ts
import { usePrivacyImage } from "expo-privacy-image";

export default function App() {
  usePrivacyImage();

  return (
    // ...
  );
}
```

When the app moves to the background, the image you configured is shown as a full-screen overlay. It is removed automatically when the app returns to the foreground.

---

## API

### `usePrivacyImage(): void`

Enables the privacy overlay for the current app session. Safe to call multiple times, subsequent calls are no-ops.

| Parameter | Type | Description |
| --------- | ---- | ----------- |
| —         | —    | No parameters |

---

## Requirements

- React Native ≥ 0.71
- expo
- iOS ≥ 15.1

