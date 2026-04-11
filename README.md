# expo-privacy-image

A React Native module that overlays a privacy image on the screen whenever the app goes to the background — preventing sensitive content from appearing in the app switcher (iOS) or recent apps (Android).

Built with [Expo Modules](https://docs.expo.dev/modules/overview/).

---

## Platform support

| Android | iOS |
| ------- | --- |
| ✅      | ✅  |

---

## Installation

```sh
npm install expo-privacy-image
```

or

```sh
yarn add expo-privacy-image
```

---

## Setup

### Expo managed / bare workflow (with `expo prebuild`)

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
- **Android** — copies the image to `android/app/src/main/res/drawable/privacy_image.png`
- **iOS** — adds the image as `PrivacyImage.imageset` inside `Images.xcassets`

### Bare React Native (manual setup)

Ensure [`expo-modules-core`](https://docs.expo.dev/bare/installing-expo-modules/) is installed before proceeding — it is required for autolinking to work.

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
    { "idiom": "universal", "filename": "privacy_image.png", "scale": "1x" }
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

Call `usePrivacyImage()` once — typically at the root of your app or inside a top-level component. Once called, the module is permanently enabled for the lifetime of the app session.

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

Enables the privacy overlay for the current app session. Safe to call multiple times — subsequent calls are no-ops.

| Parameter | Type | Description |
| --------- | ---- | ----------- |
| —         | —    | No parameters |

---

## Requirements

- React Native ≥ 0.71
- expo
- iOS ≥ 15.1

