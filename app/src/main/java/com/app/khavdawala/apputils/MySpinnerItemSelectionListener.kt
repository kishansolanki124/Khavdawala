package com.app.khavdawala.apputils

import android.view.View
import android.widget.AdapterView

abstract class MySpinnerItemSelectionListener : AdapterView.OnItemSelectedListener {

    abstract fun onUserItemSelected(parent: AdapterView<*>?, view: View?, dropdownPosition: Int, id: Long)

    private var check = 0

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View,
        dropdownPosition: Int,
        id: Long
    ) {
        if (++check > 1) {
            onUserItemSelected(parent, view, dropdownPosition, id)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}