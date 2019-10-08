package com.steamedbunx.android.camarangallerytester.ui.main

import android.Manifest
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.steamedbunx.android.camarangallerytester.R
import com.steamedbunx.android.camarangallerytester.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    lateinit var dialogPermissionListener:PermissionListener
    lateinit var binding:MainFragmentBinding

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
        dialogPermissionListener = DialogOnDeniedPermissionListener.Builder
            .withContext(requireContext())
            .withTitle("Camera permission")
            .withMessage("Camera permission is needed to take pictures of the item")
            .withButtonText(android.R.string.ok)
            .build()
        binding.buttonCamara.setOnClickListener { askPermission() }
    }

    fun askPermission(){
        Dexter.withActivity(requireActivity())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(dialogPermissionListener)
            .check()
    }

}
