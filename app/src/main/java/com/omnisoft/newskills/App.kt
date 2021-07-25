package com.omnisoft.newskills

import android.app.Application
import android.content.Context
import com.omnisoft.newskills.Others.APIs

class App : Application() {
    init {
        APIs.BASE_URL = "http://203.176.190.204:8443/"
    }
}