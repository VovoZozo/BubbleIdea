package com.example.bubbleidea.repository

import com.example.bubbleidea.database.BubbleIdeaDao
import com.example.bubbleidea.database.entites.ActivateWord
import com.example.bubbleidea.database.entites.Association
import com.example.bubbleidea.database.entites.AssociativeIdea
import com.example.bubbleidea.database.entites.OwnIdea
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BubbleIdeaRepository @Inject constructor(private val bubbleIdeaDao: BubbleIdeaDao) {
    fun getAllOwnIdeas() = bubbleIdeaDao.getAllOwnIdeas()
    fun getAllActivateWords() = bubbleIdeaDao.getAllActivateWords()
    suspend fun insertOwnIdea(ownIdea: OwnIdea) = bubbleIdeaDao.insertOwnIdea(ownIdea)
    suspend fun insertAssociativeIdea(associativeIdea: AssociativeIdea) = bubbleIdeaDao.insertAssociativeIdea(associativeIdea)
    suspend fun updateOwnIdea(ownIdea: OwnIdea) = bubbleIdeaDao.updateOwnIdea(ownIdea)
    suspend fun updateActivateWord(activateWord: ActivateWord) = bubbleIdeaDao.updateActivateWord(activateWord)
    suspend fun deleteAllOwnIdeas() = bubbleIdeaDao.deleteAllOwnIdeas()
    fun getAllAssociativeIdeasByOwnIdea(ownIdeaId: Long) = bubbleIdeaDao.getAllAssociativeIdeasByOwnIdea(ownIdeaId)
    suspend fun getSingleActivateWordByName(activateWord: String) = bubbleIdeaDao.getSingleActivateWordByName(activateWord)
    suspend fun insertActivateWord(activateWord: ActivateWord) = bubbleIdeaDao.insertActivateWord(activateWord)
    suspend fun deleteAllActivateWords() = bubbleIdeaDao.deleteAllActivateWords()
    suspend fun insertAssociation(association: Association) = bubbleIdeaDao.insertAssociation(association)
    fun getAllAssociationsByActivateWord(activateWordId: Long) = bubbleIdeaDao.getAllAssociationsByActivateWord(activateWordId)
    suspend fun getListAssociationsByActivateWord(activateWordId: Long) = bubbleIdeaDao.getListAssociationsByActivateWord(activateWordId)
    suspend fun getSingleOwnIdeaByText(ownIdea: String) = bubbleIdeaDao.getSingleOwnIdeaByText(ownIdea)
}