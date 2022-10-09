package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.View
import com.codepath.apps.restclienttemplate.models.SampleModel
import com.codepath.apps.restclienttemplate.models.SampleModelDao
import com.codepath.oauth.OAuthLoginActionBarActivity

class LoginActivity : OAuthLoginActionBarActivity<RestClient>() {

    var sampleModelDao: SampleModelDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.setTitle(Html.fromHtml("<font color=\"#1DA1F2\">" + getString(R.string.app_name) + "</font>"))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val sampleModel = SampleModel()
        sampleModel.name = "CodePath"
        sampleModelDao = (applicationContext as RestApplication).myDatabase?.sampleModelDao()
        AsyncTask.execute { sampleModelDao?.insertModel(sampleModel) }
    }


    // Inflate the menu; this adds items to the action bar if it is present.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.login, menu)
        return true
    }

    // OAuth authenticated successfully, launch primary authenticated activity
    // i.e Display application "homepage"
    override fun onLoginSuccess() {
        Log.i("Brandon", "Logged in successfully!")

        val i = Intent(this, TimelineActivity::class.java)
        startActivity(i)
    }

    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    override fun onLoginFailure(e: Exception) {
        Log.i("Brandon", "Login failed!")
        e.printStackTrace()
    }

    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    fun loginToRest(view: View?) {
        client.connect()
    }
}