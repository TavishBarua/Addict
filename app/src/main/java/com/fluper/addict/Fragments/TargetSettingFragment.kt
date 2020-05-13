package com.fluper.addict.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.fluper.addict.Addict
import com.fluper.addict.CustomFormView
import com.fluper.addict.R
import com.google.gson.Gson


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TargetSettingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TargetSettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TargetSettingFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private var mButtonSave: AppCompatButton? = null
    private var mAddict: ArrayList<Addict>? = null

    private var mCustomFormView1: CustomFormView? = null
    private var mCustomFormView2: CustomFormView? = null
    private var mCustomFormView3: CustomFormView? = null
    private var mCustomFormView4: CustomFormView? = null
    private var mCustomFormView5: CustomFormView? = null
    private var mCustomFormView6: CustomFormView? = null

    private var mSpinner: AppCompatSpinner? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_target_setting, container, false)
        mButtonSave = view.findViewById(R.id.button_save)
        mCustomFormView1 = view.findViewById(R.id.cc1)
        mCustomFormView2 = view.findViewById(R.id.cc2)
        mCustomFormView3 = view.findViewById(R.id.cc3)
        mCustomFormView4 = view.findViewById(R.id.cc4)
        mCustomFormView5 = view.findViewById(R.id.cc5)
        mCustomFormView6 = view.findViewById(R.id.cc6)
        mSpinner = view.findViewById(R.id.spinner_target)
        val targetSpinnerList: MutableList<String> = mutableListOf()
        targetSpinnerList.add("Daily")
        targetSpinnerList.add("Weekly")
        targetSpinnerList.add("Monthly")
        targetSpinnerList.add("Yearly")
        val dataAdapter: ArrayAdapter<String>? =
            context?.let {
                ArrayAdapter<String>(
                    it,
                    R.layout.simple_spinner_item,
                    targetSpinnerList
                )
            }
        dataAdapter?.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        mSpinner?.adapter = dataAdapter
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val activity = activity as AppCompatActivity?
        activity?.setSupportActionBar(toolbar)
        activity?.apply {
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = getString(R.string.str_fragment_target_setting)
        }
        toolbar.setNavigationOnClickListener(navigationListener)
        mAddict = arrayListOf()
        mButtonSave?.setOnClickListener(saveOnClickListener)

        return view
    }

    val saveOnClickListener = View.OnClickListener {
        mAddict?.clear()

        if (TextUtils.isEmpty(mCustomFormView1?.targetText?.text.toString())) {
            mCustomFormView1?.targetText?.error = getString(R.string.str_item_cannot_be_empty)
            return@OnClickListener
        } else if (TextUtils.isEmpty(mCustomFormView2?.targetText?.text.toString())) {
            mCustomFormView2?.targetText?.error = getString(R.string.str_item_cannot_be_empty)
            return@OnClickListener
        } else if (TextUtils.isEmpty(mCustomFormView3?.targetText?.text.toString())) {
            mCustomFormView3?.targetText?.error = getString(R.string.str_item_cannot_be_empty)
            return@OnClickListener
        } else if (TextUtils.isEmpty(mCustomFormView4?.targetText?.text.toString())) {
            mCustomFormView4?.targetText?.error = getString(R.string.str_item_cannot_be_empty)
            return@OnClickListener
        } else if (TextUtils.isEmpty(mCustomFormView5?.targetText?.text.toString())) {
            mCustomFormView5?.targetText?.error = getString(R.string.str_item_cannot_be_empty)
            return@OnClickListener
        } else if (TextUtils.isEmpty(mCustomFormView6?.targetText?.text.toString())) {
            mCustomFormView6?.targetText?.error = getString(R.string.str_item_cannot_be_empty)
            return@OnClickListener
        }

        mAddict?.add(
            Addict(
                getString(R.string.str_A),
                mCustomFormView1?.targetText?.text.toString().toInt()
            )
        )
        mAddict?.add(
            Addict(
                getString(R.string.str_D),
                mCustomFormView2?.targetText?.text.toString().toInt()
            )
        )
        mAddict?.add(
            Addict(
                getString(R.string.str_D),
                mCustomFormView3?.targetText?.text.toString().toInt()
            )
        )
        mAddict?.add(
            Addict(
                getString(R.string.str_I),
                mCustomFormView4?.targetText?.text.toString().toInt()
            )
        )
        mAddict?.add(
            Addict(
                getString(R.string.str_C),
                mCustomFormView5?.targetText?.text.toString().toInt()
            )
        )
        mAddict?.add(
            Addict(
                getString(R.string.str_T),
                mCustomFormView6?.targetText?.text.toString().toInt()
            )
        )

        val gson = Gson()
        val json = gson.toJson(mAddict)

        activity?.getSharedPreferences(
            getString(R.string.fluper_saved_target_list_pref),
            Context.MODE_PRIVATE
        )?.edit()
            ?.putString(getString(R.string.fluper_saved_target_list_pref_key), json)?.apply()
        val fragment = AddictMeterFragment()
        val fragmentManager: FragmentManager? = activity?.supportFragmentManager
        val fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.ll_target, fragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.remove(TargetSettingFragment())?.commitAllowingStateLoss()


    }

    val navigationListener = View.OnClickListener {
        activity?.onBackPressed()
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }


}
