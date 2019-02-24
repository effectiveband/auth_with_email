package com.opengamer.authsample

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class AuthGoogleInteractor(private val googleAuthEmailCallback: GoogleAuthEmailCallback? = null) {

    companion object {
        internal const val NO_EMAIL_ERROR = 1
        internal const val EXCEPTION_ERROR = 2

        private const val REQUEST_KEY = 1000
    }

    fun auth(activity: Activity) {
        GoogleSignIn.getLastSignedInAccount(activity)?.email?.let {
            googleAuthEmailCallback?.onLoginWithEmail(it)
            return
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val signInIntent = GoogleSignIn.getClient(activity, gso).signInIntent

        activity.startActivityForResult(signInIntent, REQUEST_KEY)
    }

    fun checkActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == REQUEST_KEY && resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            return true
        }

        return false
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.email?.also {
                googleAuthEmailCallback?.onLoginWithEmail(it)
            } ?: googleAuthEmailCallback?.onLoginWithEmailFailed(NO_EMAIL_ERROR, "No email in account ¯\\_(ツ)_/¯")
        } catch (e: ApiException) {
            googleAuthEmailCallback?.onLoginWithEmailFailed(EXCEPTION_ERROR, e.message)
        }
    }

    interface GoogleAuthEmailCallback {
        fun onLoginWithEmail(email: String)

        fun onLoginWithEmailFailed(errorCode: Int, message: String? = null)
    }
}
