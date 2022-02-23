package com.drvidal.qrscanner.data.code

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Code(
    var title: String,
    val content: String,
    var timestamp: Long,
    val isUrl: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)