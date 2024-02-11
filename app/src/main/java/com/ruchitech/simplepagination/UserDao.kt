package com.ruchitech.simplepagination

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Delete
    suspend fun delete(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM users ORDER BY id ASC LIMIT :pageSize OFFSET :offset")
  suspend  fun getUsersWithPagination(pageSize: Int, offset: Int): List<User>
}
