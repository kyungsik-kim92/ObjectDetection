package com.example.presentation.ui.search.word

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.presentation.databinding.FragmentWordDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

const val ARG_WORD = "item_word"

@AndroidEntryPoint
class WordDetailFragment : Fragment() {
    private var _binding: FragmentWordDetailBinding? = null
    private val binding get() = _binding!!

    private val wordDetailViewModel by viewModels<WordDetailViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeUiState()
    }


    private fun initUi() {
        binding.viewWordDetail.itemBookmark.setOnCheckedChangeListener { _, bookmarkState ->
            wordDetailViewModel.toggleBookmark(bookmarkState)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                wordDetailViewModel.uiState.collect { state ->
                    when (state) {
                        is WordDetailUiState.Loading -> {
                            binding.progressbar.bringToFront()
                            binding.progressbar.isVisible = true
                            binding.notResult.isVisible = false
                            binding.containerWordDetail.isVisible = false
                        }

                        is WordDetailUiState.Success -> {
                            binding.progressbar.isVisible = false
                            setupWordDetail(state)
                        }

                        is WordDetailUiState.NotFound -> {
                            binding.progressbar.isVisible = false
                            binding.notResult.isVisible = true
                            binding.containerWordDetail.isVisible = false
                        }

                        is WordDetailUiState.BookmarkUpdated -> {
                            binding.viewWordDetail.itemBookmark.isChecked = state.isBookmark
                        }

                        is WordDetailUiState.Error -> {
                            binding.progressbar.isVisible = false
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun setupWordDetail(state: WordDetailUiState.Success) {
        binding.notResult.isVisible = false
        binding.containerWordDetail.isVisible = true

        with(binding.viewWordDetail) {
            item = state.item
            phonetic.text = state.item?.phonetic

            val soundUrls = state.item?.phonetics?.filter { it.audio.isNotEmpty() } ?: emptyList()
            sound.isVisible = soundUrls.isNotEmpty()

            if (soundUrls.isNotEmpty()) {
                sound.setOnClickListener {
                    playSound(soundUrls[0].audio)
                }
            }
            containerNoun.isVisible = false
            containerVerb.isVisible = false
            containerAdjective.isVisible = false
            containerAdverb.isVisible = false

            state.item?.meanings?.forEach { meaning ->
                when (meaning.partOfSpeech) {
                    "noun" -> {
                        containerNoun.isVisible = true
                        noun.text = meaning.definitions[0].definition
                    }

                    "verb" -> {
                        containerVerb.isVisible = true
                        verb.text = meaning.definitions[0].definition
                    }

                    "adjective" -> {
                        containerAdjective.isVisible = true
                        adjective.text = meaning.definitions[0].definition
                    }

                    "adverb" -> {
                        containerAdverb.isVisible = true
                        adverb.text = meaning.definitions[0].definition
                    }
                }
            }

            itemBookmark.isChecked = state.isBookmark
        }
    }


    private fun playSound(url: String) {
        try {
            val mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
            }
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            Log.d("결과", e.message.toString())
            Toast.makeText(requireContext(), "발음 듣기를 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}