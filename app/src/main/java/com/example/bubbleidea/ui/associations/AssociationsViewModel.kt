package com.example.bubbleidea.ui.associations

import androidx.lifecycle.ViewModel
import com.example.bubbleidea.repository.BubbleIdeaRepository

class AssociationsViewModel (
    private val bubbleIdeaRepository: BubbleIdeaRepository
) : ViewModel() {

    fun getAssociations(currentActivateWordId: Long) =
        bubbleIdeaRepository.getAllAssociationsByActivateWord(currentActivateWordId)
}








