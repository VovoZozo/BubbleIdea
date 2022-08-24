package com.example.bubbleidea.ui.idea_details

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.bubbleidea.App
import com.example.bubbleidea.R
import com.example.bubbleidea.helpers.ResultState
import com.example.bubbleidea.databinding.FragmentIdeaDetailsBinding
import javax.inject.Inject

class IdeaDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: IdeaDetailsViewModelProvider

    private val binding: FragmentIdeaDetailsBinding by viewBinding()
    private val args: IdeaDetailsFragmentArgs by navArgs()
    private lateinit var adapter: IdeaDetailsRecyclerAdapter

    private lateinit var viewModel: IdeaDetailsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelProvider)[IdeaDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = IdeaDetailsRecyclerAdapter(layoutInflater)
        return inflater.inflate(R.layout.fragment_idea_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ownIdeaText.setText(args.ownIdea)

        viewModel.editedIdea.ownIdeaId = args.ownIdeaId
        viewModel.editedIdea.ownIdea = args.ownIdea

        binding.generateIdeaFab.setOnClickListener {
            viewModel.updateIdeaWithSearch()
        }

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.ownIdeaText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                viewModel.editedIdea.ownIdea = s.toString()
                viewModel.updateIdea()
            }
        })

        viewModel.updateAssociativeIdeas(args.ownIdeaId).observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        viewModel.updateIdea.observe(viewLifecycleOwner) { idea ->
            idea.getContentIfNotHandled()?.let {
                val action = IdeaDetailsFragmentDirections.actionIdeaDetailsFragmentToIdeaGeneratorFragment(
                    it.ownIdea,
                    it.ownIdeaId
                )
                findNavController().navigate(action)
            }
        }

        viewModel.addOnNavigation.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Loading -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.progress.visibility = View.VISIBLE
                }
                is ResultState.Error -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_LONG)
                        .show()
                }
                is ResultState.Success -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                }
            }
        }
    }
}