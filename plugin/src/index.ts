// plugins/withPrivacyImage.ts
import { ConfigPlugin, withDangerousMod } from "expo/config-plugins";
import * as fs from "fs";
import * as path from "path";

type PrivacyImageProps = {
  imagePath: string;
};

const withPrivacyImage: ConfigPlugin<PrivacyImageProps> = (config, { imagePath }) => {
  // Android - copy to res/drawable
  config = withDangerousMod(config, [
    "android",
    async (config) => {
      const src = path.resolve(config.modRequest.projectRoot, imagePath);
      const dest = path.join(config.modRequest.platformProjectRoot, "app/src/main/res/drawable/privacy_image.png");
      fs.mkdirSync(path.dirname(dest), { recursive: true });
      fs.copyFileSync(src, dest);
      return config;
    },
  ]);

  // iOS - copy into Images.xcassets as an imageset so UIImage(named:) works
  config = withDangerousMod(config, [
    "ios",
    async (config) => {
      const src = path.resolve(config.modRequest.projectRoot, imagePath);
      const projectName = config.modRequest.projectName!;
      const imagesetDir = path.join(
        config.modRequest.platformProjectRoot,
        projectName,
        "Images.xcassets",
        "PrivacyImage.imageset",
      );
      fs.mkdirSync(imagesetDir, { recursive: true });
      fs.copyFileSync(src, path.join(imagesetDir, "privacy_image.png"));
      const contents = {
        images: [{ idiom: "universal", filename: "privacy_image.png", scale: "1x" }],
        info: { version: 1, author: "xcode" },
      };
      fs.writeFileSync(path.join(imagesetDir, "Contents.json"), JSON.stringify(contents, null, 2));
      return config;
    },
  ]);

  return config;
};

export default withPrivacyImage;
