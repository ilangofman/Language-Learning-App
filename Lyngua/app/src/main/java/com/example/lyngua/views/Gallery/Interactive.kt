package com.example.lyngua.views.Gallery

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.*
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.activity.addCallback
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lyngua.R
import com.example.lyngua.controllers.GalleryController
import com.example.lyngua.views.Gallery.Dialogs.CreateAlbum
import com.example.lyngua.views.Gallery.Dialogs.SaveToAlbum
import com.example.lyngua.controllers.UserController
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

    private lateinit var galleryController: GalleryController
    private var user: User? = null

    private var imageCapture: ImageCapture? = null
    private var imageBitmap: Bitmap? = null
    private lateinit var fileName: String
    private var objectWord: String? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
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

        galleryController = GalleryController(
            requireContext(), requireActivity(), resources.getString(
                R.string.app_name
            )
        )
        user = UserController().readUserInfo(requireContext())

        //On back button press if image has been taken close image and display camera again
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            imageView.setImageDrawable(null)
            viewFinder.visibility = View.VISIBLE
            button_camera_capture.visibility = View.VISIBLE
            button_close.visibility = View.GONE
            button_save.visibility = View.GONE

            this.isEnabled = false
        }

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Set up the listener for take photo button
        button_camera_capture.setOnClickListener {
            takePhoto()
            callback.isEnabled = true
        }

        //Close button - Show camera and hide buttons
        button_close.setOnClickListener {
            imageView.setImageDrawable(null)
            viewFinder.visibility = View.VISIBLE
            button_camera_capture.visibility = View.VISIBLE
            button_close.visibility = View.GONE
            button_save.visibility = View.GONE
            callback.isEnabled = false
            objectWord = null
        }

        //Save button - Display save to album dialog
        button_save.setOnClickListener {
            if (objectWord != null) {
                val alertSheet = SaveToAlbum()
                alertSheet.setTargetFragment(this, REQUEST_CODE_DIALOG)
                alertSheet.show(parentFragmentManager, "alertSheetSaveToAlbum")
            } else {
                Toast.makeText(requireContext(), "Object has not been identified yet", Toast.LENGTH_SHORT).show()
            }
        }

        //Gallery button - Navigate to gallery fragment
        button_gallery.setOnClickListener {
            findNavController().navigate(R.id.action_interactive_to_gallery)
        }

        outputDirectory = galleryController.getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    /*
     * Purpose: Start the camera
     * Input:   None
     * Output:  None
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
     * Input:   None
     * Output:   None
     */
    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        fileName = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"

        imageCapture.takePicture(ContextCompat.getMainExecutor(requireContext()), object :
            ImageCapture.OnImageCapturedCallback() {

                //When image is captured convert to bitmap, display the image and show buttons for options
                @RequiresApi(Build.VERSION_CODES.N)
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

                    if (imageBitmap != null) {
                        startInteractiveModeBlackboard(imageBitmap!!)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    val errorType = exception.imageCaptureError
                    Log.e("CameraX", errorType.toString())
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //If the result is from a dialog check which dialog returned data
        if (requestCode == REQUEST_CODE_DIALOG && resultCode == Activity.RESULT_OK) {

            //If the dialog was for choosing an album to save photo to call the controller's save photo function
            if (data?.extras?.containsKey("albumName")!!) {
                val albumName = data.extras?.getString("albumName")!!

                //Show camera once image is saved to album
                if (galleryController.savePhoto(imageBitmap, albumName, fileName, objectWord!!)) {
                    Toast.makeText(requireContext(), "Photo saved in $albumName", Toast.LENGTH_SHORT).show()

                    viewFinder.visibility = View.VISIBLE
                    button_camera_capture.visibility = View.VISIBLE
                    button_close.visibility = View.GONE
                    button_save.visibility = View.GONE
                } else {
                    Toast.makeText(requireContext(), "Album not found", Toast.LENGTH_SHORT).show()
                }
            //If the dialog was for choosing to creating a new album display the create album dialog
            } else if (data.extras?.containsKey("createNewAlbum")!!) {
                val createAlbum = data.extras?.getBoolean("createNewAlbum")!!

                if (createAlbum) {
                    val alertSheet = CreateAlbum()
                    alertSheet.setTargetFragment(this, REQUEST_CODE_DIALOG)
                    alertSheet.show(parentFragmentManager, "alertSheetCreateAlbum")
                }
            //If the dialog was for creating a new album call the controllers create album function
            // and display the save to album dialog again
            } else if (data.extras?.containsKey("createAlbum")!!) {
                val albumName = data.extras?.getString("createAlbum")!!

                val response = galleryController.createAlbum(albumName)

                if (response)
                    Toast.makeText(requireContext(), "Successfully created album", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(requireContext(), "Failed to create album", Toast.LENGTH_LONG).show()

                val alertSheet = SaveToAlbum()
                alertSheet.setTargetFragment(this, REQUEST_CODE_DIALOG)
                alertSheet.show(parentFragmentManager, "alertSheetSaveToAlbum")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun startInteractiveModeBlackboard(imageBitmap: Bitmap){
    //        Toast.makeText(requireContext(), "Bitmap not null",Toast.LENGTH_SHORT).show()

        val image = InputImage.fromBitmap(imageBitmap, 0)

        val localModel = LocalModel.Builder()
            .setAssetFilePath("classification_models/image_classifier.tflite")
            .build()

        val customImageLabelerOptions = CustomImageLabelerOptions
            .Builder(localModel)
//            .setConfidenceThreshold(0.5f)
            .setMaxResultCount(1)
            .build()

        val labeler = ImageLabeling.getClient(customImageLabelerOptions)

        labeler.process(image).addOnSuccessListener { labels ->
            objectWord = "Word to Translate"

            for (label in labels) {
                objectWord = label.text
                val confidence = label.confidence
                val index = label.index

                Log.d("ImageC", "Found: $objectWord, with $confidence")
            }

            var wordOptions = galleryController.makeQuestionFromWord(
                objectWord!!,
                user!!.language.code
            )
            if(wordOptions != null) {
                val correctWord = wordOptions[0]
                wordOptions = wordOptions.shuffled(Random())
                val correctOption = wordOptions.indexOf(correctWord) + 1

                displayInteractiveQuestion(objectWord!!, wordOptions, correctOption)
            }
        }.addOnFailureListener { e ->
            Log.d("ImageC", e.toString())
            Toast.makeText(requireContext(), "Failed To  $e", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun displayInteractiveQuestion(objectWord: String, wordOptions: List<String>, correctOption: Int){
        //create the question
        val bottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)

        bottomSheet.setContentView(R.layout.interactive_question_panel)
        bottomSheet.question_title_interactive.text = objectWord.capitalize(Locale.getDefault())


        bottomSheet.option_1.text = Html.fromHtml(wordOptions[0], Html.FROM_HTML_MODE_LEGACY).toString().capitalize(Locale.getDefault())
        bottomSheet.option_2.text = Html.fromHtml(wordOptions[1], Html.FROM_HTML_MODE_LEGACY).toString().capitalize(Locale.getDefault())
        bottomSheet.option_3.text = Html.fromHtml(wordOptions[2], Html.FROM_HTML_MODE_LEGACY).toString().capitalize(Locale.getDefault())
        bottomSheet.option_4.text = Html.fromHtml(wordOptions[3], Html.FROM_HTML_MODE_LEGACY).toString().capitalize(Locale.getDefault())


        bottomSheet.option_1.setOnClickListener{
            if(correctOption == 1) showCorrectButton(bottomSheet.option_1)
            else showWrongButton(bottomSheet.option_1)
        }

        bottomSheet.option_2.setOnClickListener {
            if(correctOption == 2) showCorrectButton(bottomSheet.option_2)
            else showWrongButton(bottomSheet.option_2)
        }

        bottomSheet.option_3.setOnClickListener {
            if(correctOption == 3) showCorrectButton(bottomSheet.option_3)
            else showWrongButton(bottomSheet.option_3)
        }

        bottomSheet.option_4.setOnClickListener {
            if(correctOption == 4) showCorrectButton(bottomSheet.option_4)
            else showWrongButton(bottomSheet.option_4)
        }
        //              bottomSheet.setCanceledOnTouchOutside(false)
        bottomSheet.show()
    }

    /*
     * Purpose: Check if all required permissions are granted
     * Input:   None
     * Output:  Boolean for if all permissions have been granted
     */
    private fun allPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //Start camera if access is granted
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    //Close camera when fragment is destroyed
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "Interactive"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val REQUEST_CODE_DIALOG = 11
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private fun showCorrectButton(button: Button){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            button.background.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), R.color.green), BlendMode.SRC_ATOP)
        } else {
            button.background.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), PorterDuff.Mode.SRC_ATOP)
        }

    }
    private fun showWrongButton(button: Button){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            button.background.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), R.color.red), BlendMode.SRC_ATOP)
        } else {
            button.background.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red), PorterDuff.Mode.SRC_ATOP)
        }
    }

    /*
     * Purpose: Convert an Image object to a Bitmap
     * Input:   None
     * Output:  Bitmap representation of image
     */
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
}



