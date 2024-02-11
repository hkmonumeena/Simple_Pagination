package com.ruchitech.simplepagination

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object DatabaseModule {
    private var database: AppDatabase? = null

    fun provideAppDatabase(context: Context): AppDatabase {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "app_database"
            ).build()
        }
        return database!!
    }
}
