package com.example.bubbleidea.ui.own_ideas_list

import com.example.bubbleidea.repository.BubbleIdeaRepository
import com.example.bubbleidea.repository.NetworkWordListRepository
import com.example.bubbleidea.ui.base.BaseViewModelFactory
import javax.inject.Inject

class OwnIdeaListViewModelProvider @Inject constructor(
    private val bubbleIdeaRepository: BubbleIdeaRepository,
    private val networkWordListRepository: NetworkWordListRepository
) : BaseViewModelFactory<OwnIdeasListViewModel>(OwnIdeasListViewModel::class.java){
    override fun createViewModel(): OwnIdeasListViewModel {
        return OwnIdeasListViewModel(
            bubbleIdeaRepository,
            networkWordListRepository
        )
    }
}