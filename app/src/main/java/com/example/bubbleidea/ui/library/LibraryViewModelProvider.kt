package com.example.bubbleidea.ui.library

import com.example.bubbleidea.repository.BubbleIdeaRepository
import com.example.bubbleidea.repository.NetworkWordListRepository
import com.example.bubbleidea.ui.base.BaseViewModelFactory
import javax.inject.Inject

class LibraryViewModelProvider @Inject constructor(
    private val bubbleIdeaRepository: BubbleIdeaRepository,
    private val networkWordListRepository: NetworkWordListRepository
) : BaseViewModelFactory<LibraryViewModel>(LibraryViewModel::class.java){
    override fun createViewModel(): LibraryViewModel {
        return LibraryViewModel(
            bubbleIdeaRepository,
            networkWordListRepository
        )
    }
}