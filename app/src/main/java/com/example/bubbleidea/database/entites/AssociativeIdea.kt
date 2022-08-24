package com.example.bubbleidea.database.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AssociativeIdea(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "associativeIdeaId")
    val associativeIdeaId: Long,
    @ColumnInfo(name = "associativeIdea")
    val associativeIdea: String,
    @ColumnInfo(name = "ownIdeaCreatorId")
    val ownIdeaCreatorId: Long,
)