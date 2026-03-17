package com.example.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.presentation.base.BaseDialogFragment
import com.example.presentation.databinding.DialogChooseBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChooseDialog(
    private val chooseItem: ChooseItem,
    private val cancelable: Boolean = true,
    private val dismissCallback: () -> Unit = {}
) : BaseDialogFragment<DialogChooseBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = cancelable
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogChooseBinding {
        return DialogChooseBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

    }

    fun initUI() {
        with(binding) {
            content1.text = chooseItem.title
            content2.text = chooseItem.content
            tvCancel.text = chooseItem.negativeString
            tvDismiss.text = chooseItem.positiveString
            tvDismiss.setOnClickListener {
                dismissCallback.invoke()
                dismiss()
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
        }
    }
}