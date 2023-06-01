package com.abdallah.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abdallah.data.model.Data
import com.abdallah.data.model.DataItem

@Database(entities = [DataItem::class, Data::class], version = 2, exportSchema = false)
abstract class MyDataBase : RoomDatabase() {

    abstract fun getPrayerTimesDao(): PrayerTimesDao
    abstract fun getQiblaDirectionsDao(): QiblaDirectionsDao

    companion object {

        var myDataBase: MyDataBase? = null
        var NAME_OF_DATABASE: String = "Prayer App DataBase"

        fun init(context: Context) {


            if (myDataBase == null) {

                myDataBase = Room.databaseBuilder(context, MyDataBase::class.java, NAME_OF_DATABASE)
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }

        fun getInstance(): MyDataBase {
            return myDataBase!!
        }
    }
}