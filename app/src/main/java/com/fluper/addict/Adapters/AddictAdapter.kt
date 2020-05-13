package com.fluper.addict


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.custom_grid_view.view.*


class AddictAdapter(context: Context, height: Int) :
    RecyclerView.Adapter<AddictAdapter.AddictViewHolder>() {


    private var mAddictList: MutableList<Addict>? = null
    private var mCurrentList: MutableList<Int>? = null
    var mBuffer: MutableList<Int>? = null
    var mSharedPreferences: SharedPreferences? = null
    var mEditor: SharedPreferences.Editor? = null
    val mContext = context
    val mHeight = height


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddictViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_grid_view, parent, false)
        mSharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.fluper_saved_all_current_pref), Context.MODE_PRIVATE)
        view.minimumHeight = mHeight
        return AddictViewHolder(view)
    }


    fun updateData(data: MutableList<Addict>?, currentList: MutableList<Int>?) {
        this.mAddictList = data
        this.mCurrentList = currentList
        mBuffer = mutableListOf(0, 0, 0, 0, 0, 0)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return mAddictList?.size ?: 0
    }

    override fun onBindViewHolder(addictViewHolder: AddictViewHolder, pos: Int) {
        addictViewHolder.bindItems(mAddictList!![pos])

    }


    inner class AddictViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!),
        View.OnClickListener {

        fun bindItems(item: Addict) {
            itemView.text_view_addict.minimumHeight = (mHeight.times(0.85).toInt())
            itemView.text_view_addict.text = item.title
            itemView.text_current_counter.text = mCurrentList?.get(adapterPosition).toString()
            itemView.text_target_counter.text = item.target.toString()
            itemView.setOnClickListener(this)
        }


        override fun onClick(view: View?) {
            when (view!!.id) {
                R.id.placeholder_cardview -> {
                    var _current = view.text_current_counter.text.toString().toInt()
                    val _target = view.text_target_counter.text.toString().toInt()
                    if (_current < _target) {
                        val counter = ++_current
                        view.text_current_counter.setText(counter.toString())
                        mEditor = mSharedPreferences?.edit()
                        mEditor?.putInt(adapterPosition.toString(), counter)
                        mEditor?.apply()
                    } else {
                        sendNotification()

                    }

                }
            }
        }
    }

    fun sendNotification(){
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = NotificationCompat.Builder(mContext, channelId())

        } else {
            builder = NotificationCompat.Builder(mContext)
        }
        val mNotificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        val mBuilder= builder
            .setContentTitle(mContext.getString(R.string.str_addict))
            .setContentText(mContext.getString(R.string.str_you_are_exceeding_your_limit))
            .setSmallIcon(R.drawable.ic_launcher_background)

        mNotificationManager?.notify(1, mBuilder.build())
    }

    private fun channelId(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = mContext.getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(mContext.getString(R.string.channel_id), channelName, importance)
            (mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                notificationChannel
            )
        }

        return mContext.getString(R.string.channel_id)

    }


}