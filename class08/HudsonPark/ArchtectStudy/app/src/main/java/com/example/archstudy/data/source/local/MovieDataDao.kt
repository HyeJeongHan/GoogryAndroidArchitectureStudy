package com.example.archstudy.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDataDao {
    @Query("SELECT * FROM MovieData")
    fun getLocalData() : MutableList<MovieData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setLocalData(movieData: MovieData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: MutableList<MovieData>)

}