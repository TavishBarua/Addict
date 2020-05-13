package com.fluper.addict

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.fluper.addict.Fragments.AddictMeterFragment
import com.fluper.addict.Fragments.TargetSettingFragment

class MainActivity : AppCompatActivity(), TargetSettingFragment.OnFragmentInteractionListener,
    AddictMeterFragment.OnFragmentInteractionListener {

    private lateinit var mFragments: MutableList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFragments = mutableListOf()
        val isFirstRun: Boolean =
            getSharedPreferences(getString(R.string.fluper_saved_starting_pref), Context.MODE_PRIVATE).getBoolean(
                getString(R.string.fluper_saved_is_first_run_pref_key),
                true
            )
        if (isFirstRun) {
            addFragment(TargetSettingFragment())

        } else {
            addFragment(AddictMeterFragment())
        }
        getSharedPreferences(getString(R.string.fluper_saved_starting_pref), Context.MODE_PRIVATE)?.edit()
            ?.putBoolean(getString(R.string.fluper_saved_is_first_run_pref_key), false)?.apply()
    }


    fun addFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.ll_main, fragment)
        fragmentTransaction.commitAllowingStateLoss()

        mFragments.add(fragment)
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
