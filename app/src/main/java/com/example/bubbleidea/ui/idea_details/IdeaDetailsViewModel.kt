package com.example.bubbleidea.ui.idea_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bubbleidea.database.entites.ActivateWord
import com.example.bubbleidea.database.entites.OwnIdea
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

class IdeaDetailsViewModel(
    private val bubbleIdeaRepository: BubbleIdeaRepository,
    private val networkWordListRepository: NetworkWordListRepository
) : ViewModel(){

    var editedIdea: OwnIdea = DEFAULT_IDEA
    private val mapper = AssociationMappers()

    private val _updateIdea = MutableLiveData<Event<OwnIdea>>()
    val updateIdea: LiveData<Event<OwnIdea>>
        get() = _updateIdea

    private val _addOnNavigation = MutableLiveData<ResultState<OwnIdea>>()
    val addOnNavigation: LiveData<ResultState<OwnIdea>>
        get() = _addOnNavigation

    fun updateAssociativeIdeas(ideaId: Long) =
        bubbleIdeaRepository.getAllAssociativeIdeasByOwnIdea(ideaId)


    private fun getAssociations() {
        val listWords = mapper.ideaToListString(editedIdea)
        viewModelScope.launch(Dispatchers.IO) {
            val listForSearch = listWords.filter {
                bubbleIdeaRepository.getSingleActivateWordByName(it).isEmpty()
            }
            if (listForSearch.isNotEmpty()) {
                val chunksListForSearch = listForSearch.chunked(10)
                chunksListForSearch.forEach { partOfListForSearch ->
                    networkWordListRepository.getWords(
                        object : Callback<WordAssociationsApiResponse> {
                            override fun onResponse(
                                call: Call<WordAssociationsApiResponse>,
                                response: Response<WordAssociationsApiResponse>
                            ) {
                                response.body()?.let {
                                    val associations = mapper.toHashMapResponseWord(it)
                                    setDataToActivateWordTable(associations)
                                }
                            }

                            override fun onFailure(
                                call: Call<WordAssociationsApiResponse>,
                                t: Throwable
                            ) {

                            }
                        },
                        partOfListForSearch, QUERY_LANG_RU, QUERY_TYPE_RESPONSE
                    )
                }
            } else {
                _updateIdea.postValue(Event(editedIdea))
                _addOnNavigation.postValue(ResultState.Success(editedIdea))
            }
            }
    }

    fun setDataToActivateWordTable(associations : HashMap<String, List<ResponseWord>>) {
        viewModelScope.launch(Dispatchers.IO) {
            associations.forEach{ (key, value) ->
                val id = bubbleIdeaRepository.insertActivateWord(ActivateWord(0, key, true))
                val newActivateWord = ActivateWord(id,key, true)
                value.forEach {
                    bubbleIdeaRepository.insertAssociation(
                        mapper.responseWordToAssociation(it, newActivateWord.activateWordId)
                    )
                }
            }
            _updateIdea.postValue(Event(editedIdea))
            _addOnNavigation.postValue(ResultState.Success(editedIdea))
        }
    }

    fun updateIdeaWithSearch() {
        _addOnNavigation.value = ResultState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            bubbleIdeaRepository.updateOwnIdea(editedIdea)
        }
        getAssociations()
    }

    fun updateIdea() {
        viewModelScope.launch(Dispatchers.IO) {
            bubbleIdeaRepository.updateOwnIdea(editedIdea)
        }
    }

    companion object {
        private val DEFAULT_IDEA = OwnIdea(DEFAULT_ID, EMPTY_STRING)
    }
}