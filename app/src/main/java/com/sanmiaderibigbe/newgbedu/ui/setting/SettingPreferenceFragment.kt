package com.sanmiaderibigbe.newgbedu.ui.setting

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.sanmiaderibigbe.newgbedu.R

class SettingPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, null)
    }
}