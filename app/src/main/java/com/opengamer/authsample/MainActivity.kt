package com.opengamer.authsample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.mainFacebookButton
import kotlinx.android.synthetic.main.activity_main.mainVkButton

class MainActivity : AppCompatActivity() {

    private val authVkInteractor = AuthVkInteractor()
    private val authFbInteractor = AuthFbInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainVkButton.setOnClickListener {
            authVkInteractor.authVK(this)
        }

        mainFacebookButton.setOnClickListener {
            authFbInteractor.auth(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (authVkInteractor.checkActivityResult(requestCode, resultCode, data)) {
            return
        }

        if (authFbInteractor.checkActivityResult(requestCode, resultCode, data)) {
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
