package com.example.presentation.ui.search.word

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.model.WordItem
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.base.ViewEvent
import com.example.presentation.base.ViewState
import com.example.presentation.databinding.FragmentWordDetailBinding
import com.example.presentation.ext.showToast
import dagger.hilt.android.AndroidEntryPoint

const val ARG_WORD = "item_word"

@AndroidEntryPoint
class WordDetailFragment : BaseFragment<FragmentWordDetailBinding>(R.layout.fragment_word_detail) {
    override val viewModel by viewModels<WordDetailViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()

    }


    override fun initUi() {

        with(binding) {
            viewWordDetail.itemBookmark.setOnCheckedChangeListener { _, bookmarkState ->
                viewModel.toggleBookmark(bookmarkState)
            }
        }
    }

    override fun onChangedViewState(state: ViewState) {
        when (state) {

            is WordDetailViewState -> {
                binding.viewWordDetail.itemBookmark.isChecked = state.isBookmark
                binding.notResult.isVisible = false
                binding.containerWordDetail.isVisible = true

                binding.viewWordDetail.item = state.item

                binding.viewWordDetail.phonetic.text = state.item?.phonetic

                val getSoundUrl = state.item?.phonetics?.filter { it.audio != "" }


                binding.viewWordDetail.sound.isVisible = getSoundUrl?.isNotEmpty() ?: false

                binding.viewWordDetail.sound.setOnClickListener {
                    getSoundUrl?.get(0)?.audio?.let(::playSound)
                }
                binding.viewWordDetail.sound.setOnClickListener {
                    state.item?.phonetics?.get(0)?.audio?.let(::playSound)
                }

                state.item?.meanings?.forEach { meaning ->

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
                binding.notResult.isVisible = (state.item == null)
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = state.isLoading
            }
        }
    }

    override fun onChangeViewEvent(event: ViewEvent) {}


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
            Log.d("결과", e.message.toString())
            showToast(message = "발음 듣기를 실패하였습니다.")
        }
    }
}