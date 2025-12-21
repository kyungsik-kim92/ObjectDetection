package com.example.presentation.ui.search.text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.presentation.databinding.FragmentTextScanBinding
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TextScanFragment : Fragment() {
    private var _binding: FragmentTextScanBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TextScanViewModel by viewModels()

    private lateinit var textRecognizer: TextRecognizer
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTextRecognizer()
        initUi()
    }


    private fun initUi() {
        binding.btnBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun initTextRecognizer() {
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        textRecognizer.close()
        _binding = null
    }
}