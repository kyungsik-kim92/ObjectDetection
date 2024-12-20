package com.example.presentation.ui.search.detect

import android.content.res.Configuration
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
import androidx.fragment.app.Fragment
import com.example.presentation.databinding.FragmentCameraBinding
import com.example.presentation.ext.routePermission
import com.example.presentation.ext.routeSelectItem
import com.example.presentation.util.ObjectDetectorHelper
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.task.vision.detector.Detection
import java.util.LinkedList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class CameraFragment : Fragment(),
    ObjectDetectorHelper.DetectorListener {
    lateinit var binding: FragmentCameraBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater)
        return binding.root
    }

    private lateinit var objectDetectorHelper: ObjectDetectorHelper
    private lateinit var bitmapBuffer: Bitmap
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null

    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onResume() {
        super.onResume()
        if (!PermissionFragment.hasPermissions(requireContext())) {
            routePermission()
        } // 권한이 없으면 권한 요청 화면 이동
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    } //Fragment가 파괴 될때 Executor 종료

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObject()
        binding.viewFinder.post {
            setUpCamera()
        }

    }

    private fun initObject() {
        objectDetectorHelper =
            ObjectDetectorHelper(context = requireContext(), objectDetectorListener = this)
            // objectDetectorHelper 초기화
        cameraExecutor = Executors.newSingleThreadExecutor() // 단일 스레드 Executor 초기화
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()

                bindCameraUseCases()

            },
            ContextCompat.getMainExecutor(requireContext()) // 메인 스레드에서 실행
        )
    } // cameraProvider 초기화

    private fun bindCameraUseCases() {


        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.") // cameraProvider가 없으면 예외 발생

        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build() // 후방 카메라 선택

        preview = Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .build() // 프리뷰 비율 4:3으로 설정, 화면 회전에 맞게 설정

        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3) //이미지 분석 비율 4:3 설정
            .setTargetRotation(binding.viewFinder.display.rotation) //화면 회전에 맞게 설정
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST) //최신 이미지로 이전 이미지 덮어쓰기
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888) // 출력 이미지 형식
            .build()
            .also {
                it.setAnalyzer(cameraExecutor) { image ->
                    if (!::bitmapBuffer.isInitialized) {
                        bitmapBuffer = Bitmap.createBitmap(
                            image.width,
                            image.height,
                            Bitmap.Config.ARGB_8888
                        )
                    } // 비트맵 버퍼 초기화

                    detectObjects(image) // 객체 감지
                }
            }

        cameraProvider.unbindAll() // 카메라 사용 해제

        try {
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer) // 카메라 라이프사이클에 바인딩
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider) // 프리뷰 표시
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun detectObjects(image: ImageProxy) {
        // Copy out RGB bits to the shared bitmap buffer
        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }

        val imageRotation = image.imageInfo.rotationDegrees // 이미지 회전
        // Pass Bitmap and rotation to the object detector helper for processing and detection
        objectDetectorHelper.detect(bitmapBuffer, imageRotation) // 객체 감지
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation = binding.viewFinder.display.rotation
    } // 이미지 분석기 회전 업데이트

    override fun onError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun onResults(
        results: MutableList<Detection>?,
        inferenceTime: Long,
        imageHeight: Int,
        imageWidth: Int
    ) {

        with(binding.overlay) {
            setResults(
                results ?: LinkedList<Detection>(),
                imageHeight,
                imageWidth
            ) // 결과가 null 이면 빈 리스트
            getTouchItem { routeSelectItem(it) } // 터치된 아이템 처리
            invalidate()
        }
    } //
}