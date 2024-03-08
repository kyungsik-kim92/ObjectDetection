package com.example.objectdetection.ui.search.detect

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.objectdetection.R
import com.example.objectdetection.base.BaseFragment
import com.example.objectdetection.databinding.FragmentSelectDetectBinding
import com.example.objectdetection.ext.showToast
import dagger.hilt.android.AndroidEntryPoint


const val ARG_SELECT_ITEM = "item_select_detection"

@AndroidEntryPoint
class SelectDetectFragment :
    BaseFragment<FragmentSelectDetectBinding>(R.layout.fragment_select_detect) {

    private var selectDetectionItem: String? = null
    private val selectDetectionViewModel by viewModels<SelectDetectionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectDetectionItem = it.getString(ARG_SELECT_ITEM)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initViewModel()
    }

    private fun initUi() {

        with(binding) {
            viewWordDetail.itemBookmark.setOnCheckedChangeListener { _, bookmarkState ->
                selectDetectionViewModel.toggleBookmark(bookmarkState)
            }
        }
    }
    private fun initViewModel() {

        selectDetectionViewModel.searchMeanWord(selectDetectionItem)

        selectDetectionViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            (viewState as? SelectDetectionViewState)?.let {
                onChangedSelectDetectionViewState(it)
            }
        }

    }
    private fun onChangedSelectDetectionViewState(viewState: SelectDetectionViewState) {
        when (viewState) {

            is SelectDetectionViewState.BookmarkState -> {
                binding.viewWordDetail.itemBookmark.isChecked = viewState.isBookmark
            }

            is SelectDetectionViewState.GetSearchWord -> {
                binding.notResult.isVisible = false
                binding.containerWordDetail.isVisible = true


                binding.viewWordDetail.item = viewState.word

                binding.viewWordDetail.phonetic.text = viewState.word.phonetic

                val getSoundUrl = viewState.word.phonetics.filter { it.audio != "" }

                binding.viewWordDetail.sound.isVisible = getSoundUrl.isNotEmpty()

                binding.viewWordDetail.sound.setOnClickListener {
                    playSound(getSoundUrl[0].audio)
                }

                viewState.word.meanings.forEach { meaning ->

                    when (meaning.partOfSpeech) {

                        "noun" -> {
                            binding.viewWordDetail.containerNoun.isVisible = true
                            binding.viewWordDetail.noun.text = meaning.definitions[0].definition
                        }

                        "verb" -> {
                            binding.viewWordDetail.containerVerb.isVisible = true
                            binding.viewWordDetail.verb.text = meaning.definitions[0].definition
                        }

                        "adjective" -> {
                            binding.viewWordDetail.containerAdjective.isVisible = true
                            binding.viewWordDetail.adjective.text =
                                meaning.definitions[0].definition
                        }

                        "adverb" -> {
                            binding.viewWordDetail.containerAdverb.isVisible = true
                            binding.viewWordDetail.adverb.text = meaning.definitions[0].definition
                        }

                        else -> {}
                    }
                }
                selectDetectionViewModel.checkBookmark()
            }

            is SelectDetectionViewState.ShowToast -> {
                showToast(message = viewState.message)
            }

            is SelectDetectionViewState.NotSearchWord -> {
                binding.notResult.isVisible = true
                binding.containerWordDetail.isVisible = false
            }

            is SelectDetectionViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }

            is SelectDetectionViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }
        }
    }

    private fun playSound(url: String) {
        try {
            val mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
            }
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            showToast(message = "발음 듣기를 실패하였습니다.")
        }
    }
}