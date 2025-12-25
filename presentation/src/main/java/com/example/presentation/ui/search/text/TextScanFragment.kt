package com.example.presentation.ui.search.text

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.databinding.FragmentTextScanBinding
import com.example.presentation.ui.adapter.ScannedWordAdapter
import com.example.presentation.ui.search.word.WordActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TextScanFragment : Fragment() {
    private var _binding: FragmentTextScanBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TextScanViewModel by viewModels()

    private lateinit var textRecognizer: TextRecognizer
    private lateinit var bitmapBuffer: Bitmap
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var scannedWordAdapter: ScannedWordAdapter

    private var isProcessing = false

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
        initAdapter()
        binding.viewFinder.post {
            setUpCamera()
        }
    }


    private fun initUi() {
        binding.btnCapture.setOnClickListener {
            if (!isProcessing) {
                isProcessing = true
                binding.progressBar.visibility = View.VISIBLE
            }
        }

        binding.btnBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun initAdapter() {
        scannedWordAdapter = ScannedWordAdapter { word ->
            navigateToWordDetail(word)
        }
        binding.rvWords.apply {
            adapter = scannedWordAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        when (state) {
                            is TextScanUiState.Idle -> {
                                binding.progressBar.visibility = View.GONE
                                binding.tvScannedText.text = "카메라를 텍스트에 맞추고 촬영 버튼을 누르세요"
                                binding.rvWords.visibility = View.GONE
                            }

                            is TextScanUiState.Processing -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            is TextScanUiState.Success -> {
                                binding.progressBar.visibility = View.GONE
                                binding.tvScannedText.text = "인식된 단어를 선택하세요"
                                binding.rvWords.visibility = View.VISIBLE
                                isProcessing = false
                            }

                            is TextScanUiState.WordSelected -> {
                                navigateToWordDetail(state.word)
                                viewModel.resetState()
                            }

                            is TextScanUiState.Error -> {
                                binding.progressBar.visibility = View.GONE
                                binding.rvWords.visibility = View.GONE
                                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                                    .show()
                                isProcessing = false
                            }
                        }
                    }
                }

                launch {
                    viewModel.scannedWords.collect { words ->
                        scannedWordAdapter.submitList(words)
                    }
                }
            }
        }
    }

    private fun initTextRecognizer() {
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun navigateToWordDetail(word: String) {
        val intent = Intent(requireContext(), WordActivity::class.java).apply {
            putExtra("word", word)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        textRecognizer.close()
        _binding = null
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()
                setupCameraWithPreview()
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    private fun setupCameraWithPreview() {
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview = Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .build()

        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor) { image ->
                    if (!::bitmapBuffer.isInitialized) {
                        bitmapBuffer = createBitmap(image.width, image.height)
                    }
                    if (isProcessing) {
                        recognizeText(image)
                    } else {
                        image.close()
                    }
                }
            }
        cameraProvider.unbindAll()
        try {
            camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageAnalyzer
            )
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun recognizeText(imageProxy: ImageProxy) {
        imageProxy.use {
            bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer)
        }

        val inputImage = InputImage.fromBitmap(
            bitmapBuffer,
            imageProxy.imageInfo.rotationDegrees
        )

        textRecognizer.process(inputImage)
            .addOnSuccessListener { text ->
                viewModel.onTextScanned(text.text)
                isProcessing = false
            }
            .addOnFailureListener { e ->
                viewModel.onError(e.message ?: "텍스트 인식 실패")
                isProcessing = false
            }
    }
}