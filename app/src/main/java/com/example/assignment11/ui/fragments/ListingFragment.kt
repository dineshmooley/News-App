package com.example.assignment11.ui.fragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.assignment11.R
import com.example.assignment11.databinding.FragmentListingBinding
import com.example.assignment11.ui.adapter.ListingAdapter
import com.example.assignment11.ui.adapter.NewsListener
import com.example.assignment11.ui.viewModels.ListingViewModel
import com.example.assignment11.ui.viewModels.ListingViewModelFactory

class ListingFragment : Fragment() {
    private lateinit var binding: FragmentListingBinding
    private lateinit var viewModel: ListingViewModel
    private lateinit var adapter: ListingAdapter

    // Register a permission request callback
    private val locationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.fetchLocation()
        } else {
            // Permission denied, show dialog
            showPermissionRationaleDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listing, container, false)

        val application = requireNotNull(activity).application
        val factory = ListingViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[ListingViewModel::class.java]

        binding.viewModel = viewModel

        adapter = ListingAdapter(
            NewsListener { newsUrl ->
                viewModel.onArticleClicked(newsUrl)
            }
        )
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = adapter

        //data for initial fetching
        viewModel.newsList.observe(viewLifecycleOwner, Observer { articles ->
            articles?.let {
                adapter.submitList(it) // Update adapter data
            }
        })

        //data for searched query
        viewModel.searchQuery.observe(viewLifecycleOwner, Observer { query ->
            query?.let {
                adapter.submitList(viewModel.onSearchQueryChanged(query))
            }
        })

        viewModel.navigateToArticle.observe(viewLifecycleOwner, Observer { url ->
            url?.let {
                val action = ListingFragmentDirections.actionListingFragmentToNewsFragment(url)
                this.findNavController().navigate(action)
                viewModel.onNavigatedToArticle()
            }
        })

        viewModel.location.observe(viewLifecycleOwner, Observer { location ->
            location?.let {
                viewModel.getWeatherData(location.latitude.toString(), location.longitude.toString())
            }
        })

        requestLocationPermission()

        return binding.root
    }

    private fun requestLocationPermission() {
        locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Location Permission Needed")
            setMessage("This app requires location access to show your current location.")
            setPositiveButton("Grant") { _, _ ->
                requestLocationPermission() // Ask again if the user previously denied
            }
            setNegativeButton("Deny") { dialog, _ ->
                dialog.dismiss()
                // Handle denial logic, send to ViewModel if necessary
                Toast.makeText(requireContext(), "Access Denied!", Toast.LENGTH_SHORT).show()
            }
            create().show()
        }
    }
}
