package com.example.bubbleidea.repository

import com.example.bubbleidea.database.entites.Association
import com.example.bubbleidea.database.entites.OwnIdea
import com.example.bubbleidea.network.ResponseWord
import com.example.bubbleidea.network.WordAssociationsApiResponse

class AssociationMappers {

    fun toListResponseWord(wordAssociationsApiResponse: WordAssociationsApiResponse) : List<ResponseWord> {
        val responseWords = mutableListOf<ResponseWord>()
        wordAssociationsApiResponse.response.forEach {
            responseWords.addAll(it.items)
        }
        return responseWords
    }
    fun toHashMapResponseWord(wordAssociationsApiResponse: WordAssociationsApiResponse) : HashMap<String,List<ResponseWord>> {
        val responseWords = hashMapOf<String,List<ResponseWord>>()
        wordAssociationsApiResponse.response.forEach { activateWord ->

                responseWords[activateWord.text] = activateWord.items

        }
        return responseWords
    }
    fun responseWordToAssociation(responseWord: ResponseWord, activateWordId: Long) : Association {
        return Association(
            0L,
            responseWord.item,
            activateWordId,
            responseWord.weight,
            responseWord.pos
        )
    }
    fun ideaToListString(ownIdea: OwnIdea): List<String> {
        val str = ownIdea.ownIdea
        val regex = Regex("[^А-Яа-я0-9]")
        val del = " "
        val resultStr = regex.replace(str, " ")
        val filteredStr = resultStr.replace("\\s+".toRegex(), " ")
        return filteredStr.split(del).filter {
            it.isNotBlank()
        }
    }
}