package expo.modules.privacyimage

import android.view.ViewGroup
import android.widget.ImageView
import expo.modules.kotlin.exception.Exceptions
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class PrivacyImageModule : Module() {

  private var isPrivacyEnabled: Boolean = false
  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  private val safeCurrentActivity
    get() = appContext.currentActivity
  private val currentActivity
    get() = safeCurrentActivity ?: throw Exceptions.MissingActivity()

  private var overlayView: ImageView? = null

  private fun showOverlay() {
    currentActivity.runOnUiThread {
      if (overlayView != null) return@runOnUiThread

      val overlay =
              ImageView(currentActivity).apply {
                val drawableId =
                        currentActivity.resources.getIdentifier(
                                "privacy_image",
                                "drawable",
                                currentActivity.packageName
                        )
                setImageResource(drawableId)
                scaleType = ImageView.ScaleType.CENTER_CROP
                layoutParams =
                        ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        )
              }

      val rootView = currentActivity.window.decorView as ViewGroup
      rootView.addView(overlay)
      overlayView = overlay
    }
  }

  private fun hideOverlay() {
    currentActivity.runOnUiThread {
      val rootView = currentActivity.window.decorView as ViewGroup
      overlayView?.let {
        rootView.removeView(it)
        overlayView = null
      }
    }
  }

  override fun definition() = ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a
    // string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for
    // clarity.
    // The module will be accessible from `requireNativeModule('PrivacyImage')` in JavaScript.
    Name("PrivacyImage")

    Function("usePrivacyImage") { isPrivacyEnabled = true }

    OnActivityEntersBackground {
      // Show privacy overlay
      if (!isPrivacyEnabled) return@OnActivityEntersBackground

      showOverlay()
    }

    OnActivityEntersForeground {
      // Remove privacy overlay
      if (!isPrivacyEnabled) return@OnActivityEntersForeground

      hideOverlay()
    }
  }
}
