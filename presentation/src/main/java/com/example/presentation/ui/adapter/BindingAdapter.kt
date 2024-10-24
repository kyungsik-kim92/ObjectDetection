package com.example.presentation.ui.adapter

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter

@BindingAdapter("onEditorAction")
fun EditText.setOnEditorActionListener(listener: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            listener()
            true
        } else {
            false
        }
    }
}