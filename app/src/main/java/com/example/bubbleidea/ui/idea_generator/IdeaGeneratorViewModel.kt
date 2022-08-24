package com.example.bubbleidea.ui.idea_generator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bubbleidea.database.entites.Association
import com.example.bubbleidea.database.entites.AssociativeIdea
import com.example.bubbleidea.database.entites.OwnIdea
import com.example.bubbleidea.repository.AssociationMappers
import com.example.bubbleidea.repository.BubbleIdeaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class IdeaGeneratorViewModel(
    private val bubbleIdeaRepository: BubbleIdeaRepository
) : ViewModel() {

    var currentIdea = DEFAULT_IDEA
    private val mapper = AssociationMappers()

    private val _associativeIdes = MutableLiveData<List<AssociativeIdea>>()
    val associativeIdes: LiveData<List<AssociativeIdea>>
        get() = _associativeIdes

    fun saveIdea(idea: AssociativeIdea) {
        viewModelScope.launch(Dispatchers.IO) {
            bubbleIdeaRepository.insertAssociativeIdea(idea)
        }
    }

    fun generate() {
        viewModelScope.launch(Dispatchers.IO) {
            val associations = mutableListOf<List<Association>>()
            val listWords = mapper.ideaToListString(currentIdea)
            listWords.forEach {
                val currentWord = bubbleIdeaRepository.getSingleActivateWordByName(it)[0]
                val currentAssociations =
                    bubbleIdeaRepository.getListAssociationsByActivateWord(currentWord.activateWordId)
                associations.add(currentAssociations)
            }
            val ideas = mutableListOf<AssociativeIdea>()
            repeat(50) {
                val associativeIdeaText = mutableListOf<String>()
                var count = 0
                associations.forEach {
                    if (it.isNotEmpty()) {
                        val randomIndex = Random.nextInt(it.size)
                        val randomElement = it[randomIndex]
                        associativeIdeaText.add(randomElement.association)
                    } else {
                        associativeIdeaText.add(listWords[count])
                    }
                    count++
                }
                count = 0
                val newIdeaText = associativeIdeaText.joinToString(" ").lowercase()
                ideas.add(AssociativeIdea(0L, newIdeaText, currentIdea.ownIdeaId))
            }
            _associativeIdes.postValue(ideas)
        }
    }

    companion object {
        private val DEFAULT_IDEA = OwnIdea(0L, "")
    }
}