package com.example.bubbleidea.database.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OwnIdea(
    @ColumnInfo(name = "ownIdeaId")
    @PrimaryKey(autoGenerate = true)
    var ownIdeaId: Long,
    @ColumnInfo(name = "ownIdeaText")
    var ownIdea: String,
)