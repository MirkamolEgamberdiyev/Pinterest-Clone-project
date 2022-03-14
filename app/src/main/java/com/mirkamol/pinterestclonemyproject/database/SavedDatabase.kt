package com.mirkamol.pinterestclonemyproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mirkamol.pinterestclonemyproject.database.dao.SavedImage

@Database(entities = [Saved::class], version = 1)
abstract class SavedDatabase : RoomDatabase() {

    abstract fun savedDao(): SavedImage

    companion object {
        private var instance: SavedDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SavedDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, SavedDatabase::class.java, "saved.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}