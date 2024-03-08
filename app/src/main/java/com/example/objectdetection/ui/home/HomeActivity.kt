package com.example.objectdetection.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import com.example.objectdetection.R
import com.example.objectdetection.base.BaseActivity
import com.example.objectdetection.databinding.ActivityHomeBinding
import com.example.objectdetection.ui.adapter.FragmentPagerAdapter
import com.example.objectdetection.ui.bookmark.BookmarkFragment
import com.example.objectdetection.ui.mypage.MyPageFragment
import com.example.objectdetection.ui.search.SearchFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val homeViewModel by viewModels<HomeViewModel>()


    private val tabConfigurationStrategy =
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.text = resources.getStringArray(R.array.array_home)[position]
            tab.icon = resources.obtainTypedArray(R.array.array_home_icon).getDrawable(position)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        initViewModel()
    }

    private fun initUi() {
        val list = listOf(
            SearchFragment(),
            BookmarkFragment(),
            MyPageFragment()
        )
        val adapter = FragmentPagerAdapter(list, this)

        with(binding) {
            viewpager.adapter = adapter
            viewpager.isUserInputEnabled = false
            viewpager.offscreenPageLimit = list.size
            TabLayoutMediator(tab,viewpager,tabConfigurationStrategy).attach()
        }
    }

    private fun initViewModel() {
        homeViewModel.viewStateLiveData.observe(this) { viewState ->
            (viewState as? HomeViewState)?.let {
                onChangedHomeViewState(it)
            }
        }
    }

    private fun onChangedHomeViewState(viewState: HomeViewState) {
        when (viewState) {
            is HomeViewState.AddBookmark -> {

            }

            is HomeViewState.DeleteBookmark -> {

            }

            is HomeViewState.ShowToast -> {

            }
        }
    }

}