package com.example.objectdetection.ui.search.word

import android.media.AudioAttributes
import android.media.MediaParser
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.objectdetection.R
import com.example.objectdetection.base.BaseFragment
import com.example.objectdetection.databinding.FragmentWordDetailBinding
import com.example.objectdetection.ext.showToast
import com.example.objectdetection.ui.adapter.WordItem
import dagger.hilt.android.AndroidEntryPoint

const val ARG_WORD = "item_word"

@AndroidEntryPoint
class WordDetailFragment : BaseFragment<FragmentWordDetailBinding>(R.layout.fragment_word_detail) {
    private var wordItem: WordItem? = null

    private val wordDetailViewModel by viewModels<WordDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            wordItem = it.getParcelable(ARG_WORD)
        }
        wordDetailViewModel.wordItemObservableField.set(wordItem)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initViewModel()
    }

    private fun initUi() {

        with(binding) {
            binding.viewWordDetail.itemBookmark.setOnCheckedChangeListener { _, bookmarkState ->
                wordDetailViewModel.toggleBookmark(bookmarkState)
            }
        }

    }

    private fun initViewModel() {

        wordDetailViewModel.searchMeanWord()


        wordDetailViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            (viewState as? WordDetailViewState)?.let {
                onChangedWordDetailViewState(it)
            }
        }

    }

    private fun onChangedWordDetailViewState(viewState: WordDetailViewState) {
        when (viewState) {

            is WordDetailViewState.BookmarkState -> {
                binding.viewWordDetail.itemBookmark.isChecked = viewState.isBookmark
            }

            is WordDetailViewState.GetSearchWord -> {
                binding.notResult.isVisible = false
                binding.containerWordDetail.isVisible = true

                binding.viewWordDetail.item = viewState.word

                binding.viewWordDetail.phonetic.text = viewState.word.phonetic

                val getSoundUrl = viewState.word.phonetics.filter { it.audio != "" }

                getSoundUrl.forEach {
                    Log.d("결과", it.audio)
                }

                binding.viewWordDetail.sound.isVisible = getSoundUrl.isNotEmpty()

                binding.viewWordDetail.sound.setOnClickListener {
                    playSound(getSoundUrl[0].audio)
                }
                binding.viewWordDetail.sound.setOnClickListener {
                    playSound(viewState.word.phonetics[0].audio)
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
                wordDetailViewModel.checkBookmark()

            }

            is WordDetailViewState.ShowToast -> {
                showToast(message = viewState.message)
            }

            is WordDetailViewState.NotSearchWord -> {
                binding.notResult.isVisible = true
                binding.containerWordDetail.isVisible = false
            }

            is WordDetailViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }

            is WordDetailViewState.HideProgress -> {
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
            Log.d("결과", e.message.toString())
            showToast(message = "발음 듣기를 실패하였습니다.")
        }
    }
}