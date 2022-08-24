package com.example.bubbleidea.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bubbleidea.database.entites.ActivateWord
import com.example.bubbleidea.database.entites.Association
import com.example.bubbleidea.database.entites.AssociativeIdea
import com.example.bubbleidea.database.entites.OwnIdea

@Database(
    entities = [
        OwnIdea::class,
        AssociativeIdea::class,
        ActivateWord::class,
        Association::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BubbleIdeaDatabase : RoomDatabase() {
    abstract val bubbleIdeaDao: BubbleIdeaDao
}