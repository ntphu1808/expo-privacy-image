import ExpoModulesCore

public class AppLifecycleDelegate: ExpoAppDelegateSubscriber {

    private var overlayView: UIImageView?

    public func applicationWillResignActive(_ application: UIApplication) {
        guard PrivacyImageModule.isPrivacyEnabled else { return }
        self.showOverlay()
    }

    public func applicationDidBecomeActive(_ application: UIApplication) {
        guard PrivacyImageModule.isPrivacyEnabled else { return }
        self.hideOverlay()
    }

    private func showOverlay() {
        DispatchQueue.main.async {
            guard self.overlayView == nil,
                let window = self.getKeyWindow()
            else { return }

            let overlay = UIImageView(frame: window.bounds)
            overlay.image = UIImage(named: "PrivacyImage")
            overlay.contentMode = .scaleAspectFill
            overlay.tag = 999

            window.addSubview(overlay)
            self.overlayView = overlay
        }
    }

    private func hideOverlay() {
        DispatchQueue.main.async {
            self.overlayView?.removeFromSuperview()
            self.overlayView = nil
        }
    }

    private func getKeyWindow() -> UIWindow? {
        UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap { $0.windows }
            .first { $0.isKeyWindow }
    }
}
