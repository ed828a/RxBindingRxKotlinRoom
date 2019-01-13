package com.example.edward.rxjavaroomexercise

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single


/**
 * Created by Edward on 1/13/2019.
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User):Long

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flowable<List<User>>

    @Query("SELECT * FROM users WHERE name = :name")
    fun getUserByName(name: String): Maybe<List<User>>

    @Query("SELECT * FROM users WHERE contact_number = :contact")
    fun getUserByContact(contact: String): Single<List<User>>

}