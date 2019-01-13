package com.example.edward.rxjavaroomexercise

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by Edward on 1/13/2019.
 */
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {

    abstract fun userDoa(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase =
                INSTANCE ?: synchronized(UserDatabase::class.java){
                    INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                        UserDatabase::class.java,
                        "users.db")
                        .build()
                        .also { INSTANCE = it }
                }
    }
}