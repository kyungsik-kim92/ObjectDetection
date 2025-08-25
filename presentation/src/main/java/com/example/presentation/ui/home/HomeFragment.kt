package com.example.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.presentation.R
import com.example.presentation.databinding.FragmentHomeBinding
import com.example.presentation.ui.adapter.FragmentPagerAdapter
import com.example.presentation.ui.bookmark.BookmarkFragment
import com.example.presentation.ui.mypage.MyPageFragment
import com.example.presentation.ui.search.SearchFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModels<HomeViewModel>()

    private val tabConfigurationStrategy =
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.text = resources.getStringArray(R.array.array_home)[position]
            tab.icon = resources.obtainTypedArray(R.array.array_home_icon).getDrawable(position)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
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
            TabLayoutMediator(tab, viewpager, tabConfigurationStrategy).attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

