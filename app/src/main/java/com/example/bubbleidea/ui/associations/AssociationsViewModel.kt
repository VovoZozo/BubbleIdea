package com.example.bubbleidea.ui.associations

import androidx.lifecycle.ViewModel
import com.example.bubbleidea.repository.BubbleIdeaRepository

class AssociationsViewModel (
    private val bubbleIdeaRepository: BubbleIdeaRepository
) : ViewModel() {

    fun getAssociations(currentActivateWordId: Long) =
        bubbleIdeaRepository.getAllAssociationsByActivateWord(currentActivateWordId)

    companion object {
        private const val DEFAULT_ID = 0L
    }
}








