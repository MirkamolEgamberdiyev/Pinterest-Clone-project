package com.mirkamol.pinterestclonemyproject.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mirkamol.pinterestclonemyproject.database.Saved
@Dao
interface SavedImage {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(saved: Saved)

    @Query("SELECT * FROM Saved")
    fun getSaved(): List<Saved>
}