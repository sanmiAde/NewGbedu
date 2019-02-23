package com.sanmiaderibigbe.newgbedu.ui.setting

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sanmiaderibigbe.newgbedu.R




class SettingPreferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_settings)
    }

    companion object {
        val PREF_ACTIVATE_DAILY_NOTIFICATION = "PREF_TURN_ON_DAILY_NOTIFICATION"
    }
}