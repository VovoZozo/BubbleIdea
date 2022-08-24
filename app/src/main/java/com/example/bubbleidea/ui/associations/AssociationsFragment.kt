package com.example.bubbleidea.ui.associations

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.bubbleidea.App
import com.example.bubbleidea.R
import com.example.bubbleidea.databinding.FragmentAssociationsBinding
import javax.inject.Inject

class AssociationsFragment: Fragment(R.layout.fragment_associations) {

    @Inject
    lateinit var viewModelProvider: AssociationsViewModelProvider
    private val binding: FragmentAssociationsBinding by viewBinding()
    private lateinit var adapter: AssociationsRecyclerAdapter
    private lateinit var viewModel: AssociationsViewModel
    private val args: AssociationsFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelProvider)[AssociationsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = AssociationsRecyclerAdapter(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.getAssociations(args.activateWordId).observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
    }
}