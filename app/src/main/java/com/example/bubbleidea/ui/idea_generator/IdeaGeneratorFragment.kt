package com.example.bubbleidea.ui.idea_generator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.bubbleidea.App
import com.example.bubbleidea.R
import com.example.bubbleidea.database.entites.AssociativeIdea
import com.example.bubbleidea.database.entites.OwnIdea
import com.example.bubbleidea.databinding.FragmentIdeaGeneratorBinding
import javax.inject.Inject

class IdeaGeneratorFragment : Fragment() {
    @Inject
    lateinit var viewModelProvider: IdeaGeneratorViewModelProvider
    private val binding: FragmentIdeaGeneratorBinding by viewBinding()
    private val args: IdeaGeneratorFragmentArgs by navArgs()
    private lateinit var viewModel: IdeaGeneratorViewModel
    private lateinit var adapter: GenerateIdeaRecyclerAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelProvider)[IdeaGeneratorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = GenerateIdeaRecyclerAdapter(layoutInflater,
            object : GenerateIdeaRecyclerAdapter.SaveIdeaClickListener {
                override fun onSaveIdeaClicked(associativeIdea: AssociativeIdea) {
                    viewModel.saveIdea(associativeIdea)
                }
            })
        return inflater.inflate(R.layout.fragment_idea_generator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.currentIdea = OwnIdea(args.ideaId, args.idea)
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.associativeIdes.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
        viewModel.generate()
    }
}