package com.phinma

import android.app.Application
import com.phinma.utils.AnalyticsHelper

class PhinmaConnectApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AnalyticsHelper.initialize(this)
    }
}