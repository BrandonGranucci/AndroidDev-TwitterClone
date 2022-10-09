package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {
    lateinit var etCompose: EditText
    lateinit var btnTweet: Button

    lateinit var client: RestClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#1DA1F2")))

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)

        client = RestApplication.getRestClient(this)

        // Handling user's click on the tweet button
        btnTweet.setOnClickListener {

            // Grab context of edit text (etCompose)
            val tweetContent = etCompose.text.toString()

            // 1. Make sure Tweet is not empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this,"Empty tweets not allowed!", Toast.LENGTH_SHORT).show()
                // Look into displaying SnackBar message
            }
            else
                // 2. Make sure Tweet is under character count
                if (tweetContent.length > 280) {
                    Toast.makeText(this, "Tweet is too long! Limit is 280 characters", Toast.LENGTH_SHORT).show()
                }
                else{
                    // Make API call to Twitter to publish Tweet
                    // Toast.makeText(this, tweetContent, Toast.LENGTH_SHORT).show()
                    client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {

                        override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                            Log.i(TAG, "Successfully published tweet!")
                            // Send tweet back to Timeline Activity
                            val tweet = Tweet.fromJson(json.jsonObject)

                            val intent = Intent()
                            intent.putExtra("tweet",tweet)
                            setResult(RESULT_OK, intent)
                            finish()
                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            response: String?,
                            throwable: Throwable?
                        ) {
                            Log.e(TAG, "Failed to publish tweet", throwable)
                        }
                    })
                }



        }
    }
    companion object {
        val TAG = "ComposeActivity"
    }
}