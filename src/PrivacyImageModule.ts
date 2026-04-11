import { NativeModule, requireNativeModule } from "expo";

declare class PrivacyImageModule extends NativeModule {
  usePrivacyImage: () => void;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<PrivacyImageModule>("PrivacyImage");
