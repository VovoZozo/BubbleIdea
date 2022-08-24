package com.example.bubbleidea.ui.idea_details

import com.example.bubbleidea.repository.BubbleIdeaRepository
import com.example.bubbleidea.repository.NetworkWordListRepository
import com.example.bubbleidea.ui.base.BaseViewModelFactory
import javax.inject.Inject

class IdeaDetailsViewModelProvider @Inject constructor(
    private val bubbleIdeaRepository: BubbleIdeaRepository,
    private val networkWordListRepository: NetworkWordListRepository
) : BaseViewModelFactory<IdeaDetailsViewModel>(IdeaDetailsViewModel::class.java){
    override fun createViewModel(): IdeaDetailsViewModel {
        return IdeaDetailsViewModel(
            bubbleIdeaRepository,
            networkWordListRepository
        )
    }
}