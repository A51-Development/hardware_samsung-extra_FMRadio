package com.royna.fmradio.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import com.royna.fmradio.SharedPreferencesConst
import com.royna.fmradio.NativeFMInterface
import com.royna.fmradio.PebbleTextView
import com.royna.fmradio.R
import vendor.samsung_ext.hardware.fmradio.SetType

class PebbleLayoutAdapter(private val mContext: Context) : BaseAdapter() {
    private var mFavoriteList: List<Int> = emptyList()
    private val mFMInterface = NativeFMInterface()
    private val mListChannel = mFMInterface.mDefaultCtl.getFreqsList()
    private val mSharedPref = PreferenceManager.getDefaultSharedPreferences(mContext)

    init {
        for (k in mListChannel) {
            if (mSharedPref.getBoolean(SharedPreferencesConst.assembleFavFreq(k), false)) mFavoriteList += k
        }
    }

    override fun getCount(): Int {
        return mFavoriteList.size
    }

    override fun getItem(p: Int): Any {
        return mFavoriteList[p]
    }

    override fun getItemId(p: Int): Long {
        return p.toLong()
    }

    override fun getView(id: Int, mConvertView: View?, parent: ViewGroup?): View {
        val mAnotherConvertView = mConvertView
            ?: (mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.favorite_channel_items,
                parent,
                false
            )
        mAnotherConvertView.findViewById<PebbleTextView>(R.id.pebble_textview).apply {
            mText = (mFavoriteList[id].toFloat() / 1000).toString()
            mColor = ResourcesCompat.getColor(
                mContext.resources,
                android.R.color.system_accent1_400,
                mContext.theme
            )
            setOnClickListener {
                mFMInterface.mDefaultCtl.setValue(SetType.SET_TYPE_FM_FREQ, mFavoriteList[id])
            }
        }
        return mAnotherConvertView
    }
}
