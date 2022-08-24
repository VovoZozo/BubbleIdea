package com.example.bubbleidea.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bubbleidea.database.entites.ActivateWord
import com.example.bubbleidea.helpers.*
import com.example.bubbleidea.network.ResponseWord
import com.example.bubbleidea.network.WordAssociationsApiResponse
import com.example.bubbleidea.repository.AssociationMappers
import com.example.bubbleidea.repository.BubbleIdeaRepository
import com.example.bubbleidea.repository.NetworkWordListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibraryViewModel  (
    private val bubbleIdeaRepository: BubbleIdeaRepository,
    private val networkWordListRepository: NetworkWordListRepository
) : ViewModel() {

    var editedWord = ""

    val words: LiveData<List<ActivateWord>> = bubbleIdeaRepository.getAllActivateWords()

    private val mapper = AssociationMappers()

    private val _addNewWord = MutableLiveData<Event<ActivateWord>>()
    val addNewWord: LiveData<Event<ActivateWord>>
    get() = _addNewWord

    private val _addOnNavigation = MutableLiveData<ResultState<ActivateWord>>()
    val addOnNavigation: LiveData<ResultState<ActivateWord>>
        get() = _addOnNavigation

    fun addNewActivateWord() {
        _addOnNavigation.value = ResultState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            if (editedWord.isBlank()) {
                _addOnNavigation.postValue(ResultState.Error(EmptyQueryException()))
                return@launch
            }
            val searchWord = bubbleIdeaRepository.getSingleActivateWordByName(editedWord)
            if (searchWord.isNotEmpty()) {
                _addOnNavigation.postValue(ResultState.Error(DuplicateException()))
                return@launch
            }
            val id = bubbleIdeaRepository.insertActivateWord(ActivateWord(0, editedWord, false))

            val newActivateWord = ActivateWord(id,editedWord, false)
            getAssociations(newActivateWord)
        }
    }

    fun deleteAllActivateWords() {
        viewModelScope.launch(Dispatchers.IO) {
            bubbleIdeaRepository.deleteAllActivateWords()
        }
    }

    private fun getAssociations(activateWord: ActivateWord) {
        networkWordListRepository.getWords(object : Callback<WordAssociationsApiResponse> {
            override fun onResponse(
                call: Call<WordAssociationsApiResponse>,
                response: Response<WordAssociationsApiResponse>
            ) {
                response.body()?.let {
                    val associations = mapper.toListResponseWord(it)
                    setDataToAssociationTable(associations, activateWord)
                }
            }
            override fun onFailure(call: Call<WordAssociationsApiResponse>, t: Throwable) {
            }
        },
            listOf(activateWord.activateWord), "ru", "response")
    }

    fun setDataToAssociationTable(associations : List<ResponseWord>, activateWord: ActivateWord) {
        associations.forEach {
            viewModelScope.launch(Dispatchers.IO) {
                bubbleIdeaRepository.insertAssociation(
                    mapper.responseWordToAssociation(it, activateWord.activateWordId)
                )
                activateWord.isSearched = true
                bubbleIdeaRepository.updateActivateWord(activateWord)
            }
        }

        _addNewWord.postValue(Event(activateWord))
        _addOnNavigation.postValue(ResultState.Success(activateWord))
    }

    fun sortedByQueryWords(query: String): List<ActivateWord>{
        return if (words.value == null) {
            emptyList()
        } else {
            words.value!!.filter {
                it.activateWord.lowercase().contains(query.lowercase())
            }
        }
    }
}