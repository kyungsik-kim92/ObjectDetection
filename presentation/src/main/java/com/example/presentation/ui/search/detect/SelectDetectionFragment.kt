package com.example.presentation.ui.search.detect

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
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
import com.example.presentation.databinding.FragmentSelectDetectBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


const val ARG_SELECT_ITEM = "item_select_detection"

@AndroidEntryPoint
class SelectDetectFragment : Fragment() {

    private var _binding: FragmentSelectDetectBinding? = null
    private val binding get() = _binding!!

    private val selectDetectionViewModel by viewModels<SelectDetectionViewModel>()

    private var selectDetectionItem: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectDetectionItem = it.getString(ARG_SELECT_ITEM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectDetectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeUiState()
        selectDetectionViewModel.searchMeanWord(selectDetectionItem)
    }


    private fun initUi() {
        binding.viewWordDetail.itemBookmark.setOnCheckedChangeListener { _, bookmarkState ->
            selectDetectionViewModel.toggleBookmark(bookmarkState)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                selectDetectionViewModel.uiState.collect { state ->
                    when (state) {
                        is SelectDetectionUiState.Loading -> {
                            binding.progressbar.bringToFront()
                            binding.progressbar.isVisible = true
                        }

                        is SelectDetectionUiState.Success -> {
                            binding.progressbar.isVisible = false
                            binding.notResult.isVisible = false
                            binding.containerWordDetail.isVisible = true
                            setupWordDetail(state)
                        }

                        is SelectDetectionUiState.NotFound -> {
                            binding.progressbar.isVisible = false
                            binding.notResult.isVisible = true
                            binding.containerWordDetail.isVisible = false
                        }

                        is SelectDetectionUiState.BookmarkUpdated -> {
                            binding.viewWordDetail.itemBookmark.isChecked = state.isBookmark
                        }

                        is SelectDetectionUiState.Error -> {
                            binding.progressbar.isVisible = false
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun setupWordDetail(state: SelectDetectionUiState.Success) {
        with(binding.viewWordDetail) {
            item = state.word
            phonetic.text = state.word.phonetic

            val soundUrls = state.word.phonetics.filter { it.audio.isNotEmpty() }
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

            state.word.meanings.forEach { meaning ->
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
        }
        selectDetectionViewModel.checkBookmark()
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
            Toast.makeText(requireContext(), "발음 듣기를 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}