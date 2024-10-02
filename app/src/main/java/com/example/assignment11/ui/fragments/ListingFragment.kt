package com.example.assignment11.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listing, container, false)

        // Set up ViewModel
        val application = requireNotNull(activity).application
        val factory = ListingViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[ListingViewModel::class.java]

        // Set up RecyclerView and Adapter
        adapter = ListingAdapter(
            NewsListener { newsUrl ->
                viewModel.onArticleClicked(newsUrl)
            }
        )
        binding.recyclerView.adapter = adapter
        binding.lifecycleOwner = this

        // Observe the data
        viewModel.newsList.observe(viewLifecycleOwner, Observer { articles ->
            articles?.let {
                adapter.submitList(it) // Update adapter data
            }
        })

        viewModel.navigateToArticle.observe(viewLifecycleOwner, Observer { url ->
            url?.let {
                // Correct SafeArgs usage for navigation
                val action = ListingFragmentDirections.actionListingFragmentToNewsFragment(url)
                this.findNavController().navigate(action)
                viewModel.onNavigatedToArticle()
            }
        })


        return binding.root
    }
}
