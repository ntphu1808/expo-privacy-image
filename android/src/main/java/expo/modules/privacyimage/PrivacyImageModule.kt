package expo.modules.privacyimage

import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import expo.modules.kotlin.exception.Exceptions
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class PrivacyImageModule : Module() {

  private val safeCurrentActivity
    get() = appContext.currentActivity
  private val currentActivity
    get() = safeCurrentActivity ?: throw Exceptions.MissingActivity()

  private var overlayView: ImageView? = null
  private var focusChangeListener: ViewTreeObserver.OnWindowFocusChangeListener? = null
  
  private fun setupOverlay() {
    currentActivity.runOnUiThread {
      if (overlayView != null) return@runOnUiThread

      val drawableId = currentActivity.resources.getIdentifier(
        "privacy_image",
        "drawable",
        currentActivity.packageName
      )

      val overlay = ImageView(currentActivity).apply {
        setImageResource(drawableId)
        scaleType = ImageView.ScaleType.CENTER_CROP
        layoutParams = ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT
        )
        alpha = 0f
        elevation = Float.MAX_VALUE
      }

      val rootView = currentActivity.window.decorView as ViewGroup
      rootView.addView(overlay)
      overlayView = overlay

      val listener = ViewTreeObserver.OnWindowFocusChangeListener { hasFocus ->
        overlay.alpha = if (hasFocus) 0f else 1f
      }
      rootView.viewTreeObserver.addOnWindowFocusChangeListener(listener)
      focusChangeListener = listener
    }
  }

  private fun teardownOverlay() {
    currentActivity.runOnUiThread {
      val rootView = currentActivity.window?.decorView as? ViewGroup ?: return@runOnUiThread
      focusChangeListener?.let {
        if (rootView.viewTreeObserver.isAlive) {
          rootView.viewTreeObserver.removeOnWindowFocusChangeListener(it)
        }
        focusChangeListener = null
      }
      overlayView?.let {
        rootView.removeView(it)
        overlayView = null
      }
    }
  }

  override fun definition() = ModuleDefinition {
    Name("PrivacyImage")

    Function("usePrivacyImage") {
      setupOverlay()
    }

    OnDestroy {
      teardownOverlay()
    }
  }
}
