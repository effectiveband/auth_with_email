package com.opengamer.authsample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.android.synthetic.main.activity_main.mainFacebookButton
import kotlinx.android.synthetic.main.activity_main.mainGoogleButton
import kotlinx.android.synthetic.main.activity_main.mainResultTextView
import kotlinx.android.synthetic.main.activity_main.mainVkButton

class MainActivity : AppCompatActivity() {

    private val authVkInteractor = AuthVkInteractor(object : AuthVkInteractor.VKAuthEmailCallback {
        override fun onLoginWithEmail(token: VKAccessToken, email: String) {
            mainResultTextView.text = "VK $email"
        }

        override fun onLoginWithEmailFailed(errorCode: Int) {
            mainResultTextView.text = "VK error ${when (errorCode) {
                AuthVkInteractor.NO_EMAIL_ERROR -> "NO EMAIL IN ACCOUNT"
                else -> "UNKNOWN ERROR"
            }}"
        }
    })

    private val authFbInteractor = AuthFbInteractor(object : AuthFbInteractor.FbAuthEmailCallback {
        override fun onLoginWithEmail(email: String) {
            mainResultTextView.text = "FB $email"
        }

        override fun onLoginWithEmailFailed(errorCode: Int, message: String?) {
            mainResultTextView.text = "FB error $message"
        }
    })

    private val authGoogleInteractor = AuthGoogleInteractor(object : AuthGoogleInteractor.GoogleAuthEmailCallback {
        override fun onLoginWithEmail(email: String) {
            mainResultTextView.text = "Google $email"
        }

        override fun onLoginWithEmailFailed(errorCode: Int, message: String?) {
            mainResultTextView.text = "Google error $message"
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainVkButton.setOnClickListener {
            authVkInteractor.authVK(this)
        }

        mainFacebookButton.setOnClickListener {
            authFbInteractor.auth(this)
        }

        mainGoogleButton.setOnClickListener {
            authGoogleInteractor.auth(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (authVkInteractor.checkActivityResult(requestCode, resultCode, data)) {
            return
        }

        if (authFbInteractor.checkActivityResult(requestCode, resultCode, data)) {
            return
        }

        if (authGoogleInteractor.checkActivityResult(requestCode, resultCode, data)) {
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
