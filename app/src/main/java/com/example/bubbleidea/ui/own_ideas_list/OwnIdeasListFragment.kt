package com.example.bubbleidea.ui.own_ideas_list

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.bubbleidea.App
import com.example.bubbleidea.R
import com.example.bubbleidea.helpers.ResultState
import com.example.bubbleidea.database.entites.OwnIdea
import com.example.bubbleidea.databinding.FragmentOwnIdeasListBinding
import com.example.bubbleidea.helpers.hideKeyboardFrom
import javax.inject.Inject

class OwnIdeasListFragment : Fragment(R.layout.fragment_own_ideas_list) {

    @Inject
    lateinit var viewModelProvider: OwnIdeaListViewModelProvider
    private val binding : FragmentOwnIdeasListBinding by viewBinding()
    private lateinit var adapter: OwnIdeasRecyclerAdapter
    private lateinit var viewModel: OwnIdeasListViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelProvider)[OwnIdeasListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = OwnIdeasRecyclerAdapter(layoutInflater,
            object : OwnIdeasRecyclerAdapter.OwnIdeaClickListener {
                override fun onOwnIdeaClicked(ownIdea: OwnIdea) {
                    findNavController().navigate(
                        OwnIdeasListFragmentDirections.actionOwnIdeasListFragmentToIdeaDetailsFragment(
                            ownIdea.ownIdeaId,
                            ownIdea.ownIdea
                        )
                    )
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_library -> {
                        findNavController().navigate(
                            OwnIdeasListFragmentDirections.actionOwnIdeasListFragmentToLibraryFragment()
                        )
                        true
                    }
                    R.id.action_delete_all -> {
                        viewModel.deleteAllOwnIdeas()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.apply {
            addIdeaItemFab.setOnClickListener{
                viewModel.editedIdea = binding.searchEditText.text.toString()
                viewModel.saveIdea()
                hideKeyboardFrom(requireContext(), binding.searchEditText)
                searchEditText.clearFocus()
            }
            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    adapter.setData(
                        viewModel.onSearchQueryChanged(
                            s.toString()
                        )
                    )
                }
                override fun afterTextChanged(s: Editable) {
                }
            })
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.ownIdeas.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        viewModel.addNewOwnIdea.observe(viewLifecycleOwner) { ownIdea ->
            ownIdea.getContentIfNotHandled()?.let {
                binding.searchEditText.setText("")
                viewModel.editedIdea = ""
                    findNavController().navigate(
                    OwnIdeasListFragmentDirections.actionOwnIdeasListFragmentToIdeaDetailsFragment(
                        it.ownIdeaId,
                        it.ownIdea
                    )
                    )
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