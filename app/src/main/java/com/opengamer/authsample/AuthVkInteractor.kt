package com.opengamer.authsample

import android.app.Activity
import android.content.Intent
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope

class AuthVkInteractor(private val vkAuthEmailCallback: VKAuthEmailCallback? = null) {

    companion object {
        private const val EMAIL_KEY = "email"
        internal const val NO_EMAIL_ERROR = 11
    }

    fun authVK(activity: Activity) {
        VK.login(activity, listOf(VKScope.EMAIL))
    }

    fun checkActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (data == null) {
            return false
        }

        return VK.onActivityResult(requestCode, resultCode, data, object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                if (data.hasExtra(EMAIL_KEY)) {
                    vkAuthEmailCallback?.onLoginWithEmail(token, data.getStringExtra(EMAIL_KEY))
                    return
                }
                vkAuthEmailCallback?.onLoginWithEmailFailed(NO_EMAIL_ERROR)
            }

            override fun onLoginFailed(errorCode: Int) {
                vkAuthEmailCallback?.onLoginWithEmailFailed(errorCode)
            }
        })
    }

    interface VKAuthEmailCallback {
        fun onLoginWithEmail(token: VKAccessToken, email: String)

        fun onLoginWithEmailFailed(errorCode: Int)
    }
}
