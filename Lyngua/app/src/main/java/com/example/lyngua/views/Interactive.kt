package com.example.lyngua.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.lyngua.R
import com.example.lyngua.controllers.GalleryController
import com.example.lyngua.controllers.UserController
import com.example.lyngua.models.Languages
import com.example.lyngua.models.User.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import kotlinx.android.synthetic.main.fragment_interactive.*
import kotlinx.android.synthetic.main.interactive_question_panel.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class Interactive : Fragment() {

    private val galleryController = GalleryController()

    private var imageCapture: ImageCapture? = null
    private var imageBitmap: Bitmap? = null
    private lateinit var photoFile: File

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    lateinit var navController: NavController
    lateinit var navBar: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interactive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Set up the listener for take photo button
        button_camera_capture.setOnClickListener {
            navController = Navigation.findNavController(view)
            navBar = requireActivity().findViewById(R.id.bottomNavigationView)
            navBar.visibility = View.GONE

            takePhoto()


        }

        button_close.setOnClickListener {
            imageView.setImageDrawable(null)
            viewFinder.visibility = View.VISIBLE
            button_camera_capture.visibility = View.VISIBLE
            button_close.visibility = View.GONE
            button_save.visibility = View.GONE

            navController = Navigation.findNavController(view)
            navBar = requireActivity().findViewById(R.id.bottomNavigationView)
            navBar.visibility = View.VISIBLE
        }

        button_save.setOnClickListener {
            galleryController.savePhoto(imageBitmap, photoFile)

            viewFinder.visibility = View.VISIBLE
            button_camera_capture.visibility = View.VISIBLE
            button_close.visibility = View.GONE
            button_save.visibility = View.GONE

            Toast.makeText(requireContext(), "Photo saved in gallery", Toast.LENGTH_SHORT).show()
        }

        outputDirectory = galleryController.getOutputDirectory(requireContext(), activity, resources.getString(R.string.app_name))

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    /*
     * Purpose: Start the camera
     * Input: None
     * Output: None
     */
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    /*
     * Purpose: Capture photo and display on the ImageView
     * Input: None
     * Output: None
     */
    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        imageCapture.takePicture(ContextCompat.getMainExecutor(requireContext()), object :
            ImageCapture.OnImageCapturedCallback() {
            @SuppressLint("UnsafeExperimentalUsageError")
            override fun onCaptureSuccess(image: ImageProxy) {
                imageBitmap = image.image?.toBitmap()
                imageView.setImageBitmap(imageBitmap)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP

                viewFinder.visibility = View.GONE
                button_camera_capture.visibility = View.GONE
                button_close.visibility = View.VISIBLE
                button_save.visibility = View.VISIBLE
                // Use the image, then make sure to close it.
                image.close()



                if(imageBitmap != null){
                    Toast.makeText(requireContext(), "Bitmap not null",Toast.LENGTH_SHORT).show()

                    val image = InputImage.fromBitmap(imageBitmap!!, 0)

                    val localModel =
                        LocalModel.Builder()
                            .setAssetFilePath("classification_models/image_classifier.tflite")
//                            .setAssetFilePath("classification_models/mobilenet_v2.tflite")
//                            .setAssetFilePath("classification_models/mnasnet_1.tflite")
//                            .setAssetFilePath("classification_models/nasnet_large_1_metadata_1.tflite")
                            .build()
                    val customImageLabelerOptions = CustomImageLabelerOptions.Builder(localModel)
//                            .setConfidenceThreshold(0.5f)
                        .setMaxResultCount(1)
                        .build()
                    val labeler =
                        ImageLabeling.getClient(customImageLabelerOptions)

//                    val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
                    labeler.process(image)
                        .addOnSuccessListener { labels ->
                            var objectIdentifiedtext: String = "Word to Translate"
                            for (label in labels) {
                                objectIdentifiedtext = label.text
                                val confidence = label.confidence
                                val index = label.index
                                Log.d("ImageC", "Found: $objectIdentifiedtext, with $confidence")
//                                Toast.makeText(requireContext(), "Found image classification $objectIdentifiedtext with $confidence",Toast.LENGTH_SHORT).show()

                            }
//                            Toast.makeText(requireContext(), "Found image classification ${labels.size}",Toast.LENGTH_SHORT).show()


                            val user: User? = UserController().readUserInfo(requireContext())
                            if(user != null) {
                                //create the question
                                val bottomSheet: BottomSheetDialog =
                                    BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)

                                bottomSheet.setContentView(R.layout.interactive_question_panel)


                                bottomSheet.question_title_interactive.text = objectIdentifiedtext
                                val translated = Languages.translate(objectIdentifiedtext, user.language.code)
                                bottomSheet.option_1.text = translated
                                bottomSheet.option_2.text = "Option 2"
                                bottomSheet.option_3.text = "Option 3"
                                bottomSheet.option_4.text = "Option 4"

                                bottomSheet.setCanceledOnTouchOutside(false)
                                bottomSheet.show()
                            }

                        }
                        .addOnFailureListener { e ->
                            Log.d("ImageC", e.toString())
                            Toast.makeText(requireContext(), "Failed To  $e",Toast.LENGTH_SHORT).show()
                        }
                }

            }

            override fun onError(exception: ImageCaptureException) {
                val errorType = exception.imageCaptureError
                Log.e("CameraX", errorType.toString())
            }
        })
    }

    /*
     * Purpose: Check if all required permissions are granted
     * Input: None
     * Output: Boolean for if all permissions have been granted
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Permissions not granted by the user", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "Interactive"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}

private fun Image.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    buffer.rewind()
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

    val matrix = Matrix()
    matrix.postRotate(90F)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

