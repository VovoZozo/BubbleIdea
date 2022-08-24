package com.example.bubbleidea.ui.library

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
import com.example.bubbleidea.*
import com.example.bubbleidea.database.entites.ActivateWord
import com.example.bubbleidea.databinding.FragmentLibraryBinding
import com.example.bubbleidea.helpers.ResultState
import com.example.bubbleidea.helpers.hideKeyboardFrom
import com.example.bubbleidea.helpers.isInternetAvailable
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class LibraryFragment : Fragment(R.layout.fragment_library) {

    private val binding: FragmentLibraryBinding by viewBinding()
    @Inject
    lateinit var viewModelProvider: LibraryViewModelProvider
    private lateinit var adapter: LibraryRecyclerAdapter
    private lateinit var viewModel: LibraryViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelProvider)[LibraryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = LibraryRecyclerAdapter(layoutInflater,
            object : LibraryRecyclerAdapter.ActivateWordClickListener {
                override fun onActivateWordClicked(activateWord: ActivateWord) {
                    hideKeyboardFrom(requireContext(), binding.searchEditText)
                    binding.searchEditText.clearFocus()
                    binding.searchEditText.setText("")
                    viewModel.editedWord = ""
                    val action =
                        LibraryFragmentDirections.actionLibraryFragmentToAssociationsFragment(
                            activateWord.activateWordId, activateWord.activateWord
                        )
                    findNavController().navigate(action)
                }

            })
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
                        true
                    }
                    R.id.action_delete_all -> {
                        viewModel.deleteAllActivateWords()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.apply {
            addWordItemFab.setOnClickListener {
                if (isInternetAvailable(requireContext())) {
                    viewModel.addNewActivateWord()
                    hideKeyboardFrom(requireContext(), binding.searchEditText)
                    searchEditText.clearFocus()
                } else {
                    Snackbar.make(
                        binding.root,
                        "Please check your internet connection",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?, start: Int, before: Int, count: Int
                ) {
                    val query = s.toString()
                    viewModel.editedWord = query
                    val searchWords = viewModel.sortedByQueryWords(query)
                    adapter.setData(searchWords)
                }

                override fun afterTextChanged(s: Editable) {
                    val query = s.toString()
                    viewModel.editedWord = query
                }
            })
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.addNewWord.observe(viewLifecycleOwner) { activateWord ->
            activateWord.getContentIfNotHandled()?.let {
                binding.searchEditText.setText("")
                viewModel.editedWord = ""
                val action =
                    LibraryFragmentDirections.actionLibraryFragmentToAssociationsFragment(
                        it.activateWordId,
                        it.activateWord
                    )
                findNavController().navigate(action)
            }
        }

        viewModel.words.observe(viewLifecycleOwner) {
            adapter.setData(it)
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