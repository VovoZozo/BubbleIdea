package com.example.bubbleidea.ui.own_ideas_list

import android.util.Log
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

class OwnIdeasListViewModel(private val bubbleIdeaRepository: BubbleIdeaRepository,
                            private val networkWordListRepository: NetworkWordListRepository) : ViewModel(){

    val ownIdeas: LiveData<List<OwnIdea>> = bubbleIdeaRepository.getAllOwnIdeas()
    private val mapper = AssociationMappers()
    var editedIdea = ""

    private val _addNewOwnIdea = MutableLiveData<Event<OwnIdea>>()
    val addNewOwnIdea: LiveData<Event<OwnIdea>>
        get() = _addNewOwnIdea

    private val _addOnNavigation = MutableLiveData<ResultState<OwnIdea>>()
    val addOnNavigation: LiveData<ResultState<OwnIdea>>
        get() = _addOnNavigation

    fun saveIdea() {
        _addOnNavigation.value = ResultState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            if (editedIdea.isBlank()) {
                _addOnNavigation.postValue(ResultState.Error(EmptyQueryException()))
                return@launch
            }
            val searchIdea = bubbleIdeaRepository.getSingleOwnIdeaByText(editedIdea)
            if (searchIdea.isNotEmpty()) {
                _addOnNavigation.postValue(ResultState.Error(DuplicateException()))
                return@launch
            }
            val id = bubbleIdeaRepository.insertOwnIdea(OwnIdea(0L, editedIdea))
            val newIdea = OwnIdea(id, editedIdea)
            getAssociations(newIdea)
        }
    }

    private fun getAssociations(newIdea: OwnIdea) {
        val listWords = mapper.ideaToListString(newIdea)
        viewModelScope.launch(Dispatchers.IO) {
            val listForSearch = listWords.filter {
                bubbleIdeaRepository.getSingleActivateWordByName(it).isEmpty()
            }
            if (listForSearch.isNotEmpty()) {
                networkWordListRepository.getWords(object : Callback<WordAssociationsApiResponse> {
                    override fun onResponse(
                        call: Call<WordAssociationsApiResponse>,
                        response: Response<WordAssociationsApiResponse>
                    ) {
                        response.body()?.let {
                            val associations = mapper.toHashMapResponseWord(it)
                            setDataToActivateWordTable(associations, newIdea)
                        }
                    }
                    override fun onFailure(call: Call<WordAssociationsApiResponse>, t: Throwable) {

                    }
                },
                    listForSearch, QUERY_LANG_RU, QUERY_TYPE_RESPONSE)
            } else {
                _addNewOwnIdea.postValue(Event(newIdea))
                _addOnNavigation.postValue(ResultState.Success(newIdea))
            }
        }
    }

    fun setDataToActivateWordTable(associations : HashMap<String, List<ResponseWord>>, newIdea: OwnIdea) {
        viewModelScope.launch {
            associations.forEach{ (key, value) ->
                val id = bubbleIdeaRepository.insertActivateWord(ActivateWord(0, key, true))
                val newActivateWord = ActivateWord(id,key, true)
                value.forEach {
                    bubbleIdeaRepository.insertAssociation(
                        mapper.responseWordToAssociation(it, newActivateWord.activateWordId)
                    )
                }
            }
            _addNewOwnIdea.postValue(Event(newIdea))
            _addOnNavigation.postValue(ResultState.Success(newIdea))
        }
    }


    fun deleteAllOwnIdeas() {
        viewModelScope.launch(Dispatchers.IO) {
            bubbleIdeaRepository.deleteAllOwnIdeas()
        }
    }

    fun onSearchQueryChanged(query: String) : List<OwnIdea> {
        return if (ownIdeas.value == null) {
            emptyList()
        } else {
            ownIdeas.value!!.filter {
                it.ownIdea.lowercase().contains(query.lowercase())
            }
        }
    }
}