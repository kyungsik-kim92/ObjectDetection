package com.example.objectdetection.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.objectdetection.BR
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    Fragment() {

    protected lateinit var binding: B

    abstract val viewModel: BaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)
        initUi()
        viewModel.viewState.map(::onChangedViewState).launchIn(viewLifecycleOwner.lifecycleScope)
        // 처음에 flow를 사용하는데 collect 하는 부분이 없어서 의아했는데 launchIn 내부에 collect가 있다는 것을 알게 됨

        viewModel.viewEvent.map(::onChangeViewEvent).launchIn(viewLifecycleOwner.lifecycleScope)
        return binding.root
    }
    abstract fun initUi()

    abstract fun onChangedViewState(state: ViewState)

    abstract fun onChangeViewEvent(event: ViewEvent)

}