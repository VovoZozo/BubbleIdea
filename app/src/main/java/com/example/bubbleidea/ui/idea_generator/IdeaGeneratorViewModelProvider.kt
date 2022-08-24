package com.example.bubbleidea.ui.idea_generator

import com.example.bubbleidea.repository.BubbleIdeaRepository
import com.example.bubbleidea.ui.base.BaseViewModelFactory
import javax.inject.Inject

class IdeaGeneratorViewModelProvider @Inject constructor(
    private val bubbleIdeaRepository: BubbleIdeaRepository
) : BaseViewModelFactory<IdeaGeneratorViewModel>(IdeaGeneratorViewModel::class.java){
    override fun createViewModel(): IdeaGeneratorViewModel {
        return IdeaGeneratorViewModel(
            bubbleIdeaRepository
        )
    }
}