package com.example.devproject.activity

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.devproject.R

class ShowWebViewActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val webView : WebView = findViewById(R.id.conferWebView)
        val conferenceURL = this.intent.getStringExtra("conferenceURL")

        webView.loadUrl(conferenceURL!!)
    }
}