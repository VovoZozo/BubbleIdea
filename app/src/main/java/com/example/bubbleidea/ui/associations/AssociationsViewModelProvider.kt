package com.example.bubbleidea.ui.associations

import com.example.bubbleidea.repository.BubbleIdeaRepository
import com.example.bubbleidea.ui.base.BaseViewModelFactory
import javax.inject.Inject

class AssociationsViewModelProvider @Inject constructor(
    private val bubbleIdeaRepository: BubbleIdeaRepository
) : BaseViewModelFactory<AssociationsViewModel>(AssociationsViewModel::class.java){
    override fun createViewModel(): AssociationsViewModel {
        return AssociationsViewModel(
            bubbleIdeaRepository
        )
    }
}