package kz.nu.connectionphoneapp.vital_sign_irt.webrtc

import android.content.Context
import android.util.AttributeSet
import org.webrtc.SurfaceViewRenderer

class WebRtcSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SurfaceViewRenderer(context, attrs) {
    
    private var isInitialized = false
    
    fun initialize(eglContext: org.webrtc.EglBase.Context?) {
        if (!isInitialized && eglContext != null) {
            init(eglContext, null)
            setEnableHardwareScaler(true)
            setMirror(false)
            isInitialized = true
        }
    }
    
    override fun onDetachedFromWindow() {
        release()
        isInitialized = false
        super.onDetachedFromWindow()
    }
}