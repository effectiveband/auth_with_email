package com.opengamer.authsample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class AuthFbInteractor(private val fbAuthEmailCallback: FbAuthEmailCallback? = null) {

    companion object {
        private const val EMAIL_PERMISSION_STRING = "email"

        private const val CANCELED_ERROR = 1
        private const val EXCEPTION_ERROR = 2
        private const val EXCEPTION_DURING_GETTING_EMAIL_ERROR = 3
    }

    private val callbackManager = CallbackManager.Factory.create()
    private val loginCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult) {
            sendEmailRequest(result.accessToken)
        }

        override fun onCancel() {
            fbAuthEmailCallback?.onLoginWithEmailFailed(CANCELED_ERROR)
        }

        override fun onError(error: FacebookException) {
            fbAuthEmailCallback?.onLoginWithEmailFailed(EXCEPTION_ERROR, error.message)
        }
    }

    init {
        LoginManager.getInstance().registerCallback(callbackManager, loginCallback)
    }

    fun auth(activity: Activity) {
        LoginManager.getInstance().logInWithReadPermissions(activity, listOf(EMAIL_PERMISSION_STRING))
    }

    fun checkActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun sendEmailRequest(accessToken: AccessToken) {
        val request =
            GraphRequest.newMeRequest(accessToken) { jsonObject, response ->
                response.error?.let {
                    fbAuthEmailCallback?.onLoginWithEmailFailed(EXCEPTION_DURING_GETTING_EMAIL_ERROR, it.errorUserMessage)
                    return@newMeRequest
                }

                val email = jsonObject.getString("email")
                fbAuthEmailCallback?.onLoginWithEmail(email)
            }
        val parameters = Bundle()
        parameters.putString("fields", "email")
        request.parameters = parameters
        request.executeAsync()
    }

    interface FbAuthEmailCallback {
        fun onLoginWithEmail(email: String)

        fun onLoginWithEmailFailed(errorCode: Int, message: String? = null)
    }
}
