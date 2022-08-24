package com.example.bubbleidea.di

import android.content.Context
import androidx.room.Room
import com.example.bubbleidea.database.BubbleIdeaDao
import com.example.bubbleidea.database.BubbleIdeaDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideDB() : BubbleIdeaDao {
        return getInstance(context).bubbleIdeaDao
    }

    companion object {
        @Volatile
        private var INSTANCE: BubbleIdeaDatabase? = null

        fun getInstance(context: Context): BubbleIdeaDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BubbleIdeaDatabase::class.java,
                    "bubble_idea_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}