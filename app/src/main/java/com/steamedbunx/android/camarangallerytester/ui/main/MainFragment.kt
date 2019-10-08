package com.steamedbunx.android.camarangallerytester.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.steamedbunx.android.camarangallerytester.R
import com.steamedbunx.android.camarangallerytester.REQUESTCODE_CAMERA
import com.steamedbunx.android.camarangallerytester.databinding.MainFragmentBinding
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {

    lateinit var binding: MainFragmentBinding

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.buttonCamera.setOnClickListener { onCameraClicked() }
        binding.buttonGallery.setOnClickListener { onGalleryClicked() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun onCameraClicked() {
        askCameraPermission()
        if (checkCameraPermission()) {
            lunchCameraIntent()
        }
    }

    fun onGalleryClicked() {
        askStoragePermission()
        if (checkStoragePermission()) {
            lunchGalleryIntent()
        }
    }

    fun askCameraPermission() {
        val dialogPermissionListener = DialogOnDeniedPermissionListener.Builder
            .withContext(requireContext())
            .withTitle("Camera permission")
            .withMessage("Camera permission is needed to take pictures of the item")
            .withButtonText(android.R.string.ok)
            .build()
        Dexter.withActivity(requireActivity())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(dialogPermissionListener)
            .check()
    }

    fun askStoragePermission() {
        val dialogPermissionListener = DialogOnDeniedPermissionListener.Builder
            .withContext(requireContext())
            .withTitle("Read Storage permission")
            .withMessage("Read Storage permission is needed to load image from gallery")
            .withButtonText(android.R.string.ok)
            .build()
        Dexter.withActivity(requireActivity())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(dialogPermissionListener)
            .check()
    }

    fun checkStoragePermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkCameraPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun lunchCameraIntent() {
//        Snackbar.make(requireView(), "Camera Intent Lunch here", Snackbar.LENGTH_LONG).show()
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUESTCODE_CAMERA)
    }

    fun lunchGalleryIntent() {
        Snackbar.make(requireView(), "Gallery Intent Lunch here", Snackbar.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUESTCODE_CAMERA -> updateBitmap(data?.extras?.get("data") as Bitmap)
            else -> return
        }
    }

    fun updateBitmap(newBitmap: Bitmap?){
        if(newBitmap != null){
            imageView.setImageBitmap(newBitmap)
        }
    }
}
