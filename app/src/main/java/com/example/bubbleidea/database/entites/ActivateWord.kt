package com.example.bubbleidea.database.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActivateWord(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "activateWordId")
    var activateWordId: Long,
    @ColumnInfo(name = "activateWordText")
    var activateWord: String,
    @ColumnInfo(name = "isSearched")
    var isSearched: Boolean
)