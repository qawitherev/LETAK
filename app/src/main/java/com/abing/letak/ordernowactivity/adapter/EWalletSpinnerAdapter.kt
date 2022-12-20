package com.abing.letak.ordernowactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.abing.letak.R

class EWalletSpinnerAdapter(context: Context, eWallet: Array<String>): ArrayAdapter<String>(context, 0, eWallet){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val eWallet = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.parking_space_spinner_item, parent, false)
        view.findViewById<TextView>(R.id.space_type_text).text = eWallet
        return view
    }
}