package com.fluper.addict.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fluper.addict.Addict
import com.fluper.addict.AddictAdapter
import com.fluper.addict.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class AddictMeterFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var numberOfColumns: Int? = null
    var recyclerView: RecyclerView? = null
    var mContext: Context? = null
    var mAddictAdapter: AddictAdapter? = null
    var mList: ArrayList<Addict>? = null
    var mResetButton: AppCompatImageButton? = null
    var mSettingButton: AppCompatImageButton? = null
    var mToolbar: Toolbar? = null


    var mSharedPreferences: SharedPreferences? = null
    var mCounterList: MutableList<Int>? = null


    companion object {
        const val ORIENTATION_PORTRAIT = 0
        const val ORIENTATION_LANDSCAPE = 1

        const val REGULAR_SCREEN_PORTRAIT = 0
        const val REGULAR_SCREEN_LANDSCAPE = 1
        const val SMALL_TABLET_PORTRAIT = 2
        const val SMALL_TABLET_LANDSCAPE = 3
        const val LARGE_TABLET_PORTRAIT = 4
        const val LARGE_TABLET_LANDSCAPE = 5
        const val XLARGE_TABLET_PORTRAIT = 6
        const val XLARGE_TABLET_LANDSCAPE = 7

        const val REGULAR = "regular"
        const val SMALL_TABLET = "small_tablet"
        const val LARGE_TABLET = "large_tablet"
        const val XLARGE_TABLET = "xlarge_tablet"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = context
        numberOfColumns = getNumberOfColumns
        val gson = Gson()
        val strJson = activity?.getSharedPreferences(getString(R.string.fluper_saved_target_list_pref), Context.MODE_PRIVATE)?.getString(getString(R.string.fluper_saved_target_list_pref_key),"")
        val type: Type = object : TypeToken<ArrayList<Addict?>?>() {}.type
        mList = gson.fromJson(strJson,type)
        mCounterList = mutableListOf(0, 0, 0, 0, 0, 0)
        mSharedPreferences = activity?.getSharedPreferences(getString(R.string.fluper_saved_all_current_pref), Context.MODE_PRIVATE)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_addict_meter, container, false)

        for (i in mCounterList?.indices!!) {
            mSharedPreferences?.getInt(i.toString(), 0)?.let { mCounterList?.set(i, it) }
        }

        mToolbar = view.findViewById(R.id.toolbar)
        val activity = activity as AppCompatActivity?
        activity?.setSupportActionBar(mToolbar)
        activity?.supportActionBar?.title = getString(R.string.str_fragment_addict_meter)
        activity?.supportActionBar?.setHomeButtonEnabled(false)
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        mResetButton = mToolbar?.findViewById(R.id.img_button_reset)
        mResetButton?.setOnClickListener(resetOnClickListener)
        mSettingButton = mToolbar?.findViewById(R.id.img_button_setting)
        mSettingButton?.setOnClickListener(settingOnClickListener)

        mAddictAdapter = AddictAdapter( mContext!!,getItemHeight())
        recyclerView = view.findViewById(R.id.rv_list)
        recyclerView?.layoutManager = GridLayoutManager(mContext, numberOfColumns!!)
        recyclerView?.adapter = mAddictAdapter

        return view
    }

    val resetOnClickListener = View.OnClickListener {

        mSharedPreferences?.edit()?.clear()?.commit()
        for (i in mCounterList?.indices!!) {
            mCounterList?.set(i, 0)
        }
        mAddictAdapter?.updateData(mList, mCounterList)
        Toast.makeText(mContext, R.string.str_reset_default, Toast.LENGTH_SHORT).show()

    }

    val settingOnClickListener = View.OnClickListener {
        val fragment = TargetSettingFragment()
        val fragmentManager: FragmentManager? = activity?.supportFragmentManager
        val fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.ll_addict, fragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.remove(AddictMeterFragment())?.commitAllowingStateLoss()
    }


    override fun onResume() {
        super.onResume()
        mAddictAdapter?.updateData(mList, mCounterList)
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


    private val getNumberOfColumns: Int
        get() {
            val config = getDeviceScreenConfiguration()
            if (config == REGULAR_SCREEN_PORTRAIT) {
                return 2
            } else if (config == LARGE_TABLET_LANDSCAPE) {
                return 6
            } else if (config == LARGE_TABLET_PORTRAIT) {
                return 4
            } else if (config == REGULAR_SCREEN_LANDSCAPE) {
                return 4
            } else if (config == XLARGE_TABLET_LANDSCAPE) {
                return 8

            } else if (config == XLARGE_TABLET_PORTRAIT) {
                return 6
            }
            return 2
        }

    fun getDeviceScreenConfiguration(): Int {
        val screenSize = getString(R.string.str_regular)
        var landscape = false
        if (orientation == ORIENTATION_LANDSCAPE) {
            landscape = true
        }

        return if (screenSize == REGULAR && !landscape)
            REGULAR_SCREEN_PORTRAIT
        else if (screenSize == REGULAR && landscape)
            REGULAR_SCREEN_LANDSCAPE
        else if (screenSize == SMALL_TABLET && !landscape)
            SMALL_TABLET_PORTRAIT
        else if (screenSize == SMALL_TABLET && landscape)
            SMALL_TABLET_LANDSCAPE
        else if (screenSize == LARGE_TABLET && !landscape)
            LARGE_TABLET_PORTRAIT
        else if (screenSize == LARGE_TABLET && landscape)
            LARGE_TABLET_LANDSCAPE
        else if (screenSize == XLARGE_TABLET && !landscape)
            XLARGE_TABLET_PORTRAIT
        else if (screenSize == XLARGE_TABLET && landscape)
            XLARGE_TABLET_LANDSCAPE
        else
            REGULAR_SCREEN_PORTRAIT
    }

    val orientation: Int
        get() {

            if ((resources.displayMetrics?.widthPixels!! > resources.displayMetrics?.heightPixels!!)) {
                return ORIENTATION_LANDSCAPE
            } else {
                return ORIENTATION_PORTRAIT
            }
        }

    private fun getItemHeight(): Int {
        val metrics = Resources.getSystem().displayMetrics
        return (getActionBarHeight(mContext)?.let { metrics.heightPixels.minus(it) })?.div((mCounterList?.size?.div(getNumberOfColumns))!!)!!
    }

    fun getActionBarHeight(context: Context?): Int? {
        val textSizeAttr = intArrayOf(R.attr.actionBarSize)
        val a = context?.obtainStyledAttributes(TypedValue().data, textSizeAttr)
        val height = a?.getDimensionPixelSize(0, 0)
        a?.recycle()
        return height
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }


}
