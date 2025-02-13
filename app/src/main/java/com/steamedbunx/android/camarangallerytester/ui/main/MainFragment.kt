package com.steamedbunx.android.camarangallerytester.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.steamedbunx.android.camarangallerytester.R
import com.steamedbunx.android.camarangallerytester.REQUESTCODE_CAMERA
import com.steamedbunx.android.camarangallerytester.REQUESTCODE_GALLERY
import com.steamedbunx.android.camarangallerytester.adapter.main.ImageListAdapter
import com.steamedbunx.android.camarangallerytester.adapter.main.ImageListListeners
import com.steamedbunx.android.camarangallerytester.data.ImageStored
import com.steamedbunx.android.camarangallerytester.databinding.MainFragmentBinding
import kotlinx.android.synthetic.main.main_fragment.*
import java.io.File
import java.net.URL


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
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerViewImages.layoutManager = linearLayoutManager


        // setup Listeners
        val listeners = object : ImageListListeners {

            override fun buttonDeleteOnClickListener(item: ImageStored) {
                viewModel.removeImage(item)
            }

            override fun buttonCropOnClickListener() {
                // not implemented but no breaking
            }

        }

        // created an adapter with the Listeners
        val adapter = ImageListAdapter(listeners)
        binding.recyclerViewImages.adapter = adapter

        viewModel.images.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
                Snackbar.make(requireView(), "update", Snackbar.LENGTH_SHORT).show()
            }

        })

        binding.setLifecycleOwner(this)
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
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUESTCODE_CAMERA)
        }
    }

    fun lunchGalleryIntent() {
//        Snackbar.make(requireView(), "Gallery Intent Lunch here", Snackbar.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUESTCODE_GALLERY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUESTCODE_CAMERA -> updateBitmapFromCamera(data?.extras?.get("data") as Bitmap)
                REQUESTCODE_GALLERY -> updateBitmapFromGallery(data?.data as Uri)
                else -> return
            }
        }
    }

    fun updateBitmapFromCamera(newBitmap: Bitmap?) {
        if (newBitmap != null) {
            imageView.setImageBitmap(newBitmap)
            viewModel.addImage(newBitmap)
        }
    }

    fun updateBitmapFromGallery(newImageUri: Uri) {
        if (newImageUri != null) {
            if (android.os.Build.VERSION.SDK_INT >= 29) {
                var bitmap =
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(File(newImageUri.path)))
                imageView.setImageBitmap(bitmap)
                viewModel.addImage(bitmap)
            } else {
                var bitmap =
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, newImageUri)
                imageView.setImageBitmap(bitmap)
                viewModel.addImage(bitmap)
            }
        }
    }
}
