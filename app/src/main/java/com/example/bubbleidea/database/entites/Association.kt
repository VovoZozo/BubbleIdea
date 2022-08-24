package com.example.bubbleidea.database.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Association(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "associationId")
    val associationId: Long,
    @ColumnInfo(name = "association")
    val association: String,
    @ColumnInfo(name = "activateWordCreatorId")
    val activateWordCreatorId: Long,
    @ColumnInfo(name = "weight")
    val weight: Int,
    @ColumnInfo(name = "pos")
    val pos: String,
)