package com.example.bubbleidea.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bubbleidea.database.entites.ActivateWord
import com.example.bubbleidea.database.entites.Association
import com.example.bubbleidea.database.entites.AssociativeIdea
import com.example.bubbleidea.database.entites.OwnIdea

@Dao
interface BubbleIdeaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwnIdea (ownIdea: OwnIdea) : Long

    @Query("SELECT * FROM ownIdea")
    fun getAllOwnIdeas() : LiveData<List<OwnIdea>>

    @Update
    suspend fun updateOwnIdea(ownIdea: OwnIdea)

    @Delete
    suspend fun deleteOwnIdea(ownIdea: OwnIdea)

    @Query("DELETE FROM ownIdea")
    suspend fun deleteAllOwnIdeas()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssociativeIdea(associativeIdea: AssociativeIdea)

    @Update
    suspend fun updateAssociativeIdea(associativeIdea: AssociativeIdea)

    @Delete
    suspend fun deleteAssociativeIdea(associativeIdea: AssociativeIdea)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivateWord(activateWord: ActivateWord) : Long

    @Query("SELECT * FROM activateWord")
    fun getAllActivateWords() : LiveData<List<ActivateWord>>

    @Query("SELECT * FROM activateWord WHERE activateWordText LIKE :activateWord")
    suspend fun getSingleActivateWordByName(activateWord: String) : List<ActivateWord>

    @Query("DELETE FROM activateWord")
    suspend fun deleteAllActivateWords()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssociation(association: Association)

    @Query("SELECT * FROM association WHERE activateWordCreatorId LIKE :activateWordCreatorId")
    fun getAllAssociationsByActivateWord(activateWordCreatorId: Long) : LiveData<List<Association>>

    @Query("SELECT * FROM associativeIdea WHERE ownIdeaCreatorId LIKE :ownIdeaCreatorId")
    fun getAllAssociativeIdeasByOwnIdea(ownIdeaCreatorId: Long) : LiveData<List<AssociativeIdea>>

    @Query("SELECT * FROM association WHERE activateWordCreatorId LIKE :activateWordCreatorId")
    suspend fun getListAssociationsByActivateWord(activateWordCreatorId: Long) : List<Association>

    @Query("SELECT * FROM association")
    fun getAllAssociations() : LiveData<List<Association>>

    @Query("SELECT * FROM ownIdea WHERE ownIdeaText LIKE :ownIdea")
    suspend fun getSingleOwnIdeaByText(ownIdea: String) : List<OwnIdea>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateActivateWord(activateWord: ActivateWord)

}