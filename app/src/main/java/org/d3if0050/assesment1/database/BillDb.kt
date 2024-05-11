package org.d3if0050.assesment1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.d3if0050.assesment1.model.Bill

@Database(entities = [Bill::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BillDb : RoomDatabase() {

    abstract val dao: BillDao

    companion object {

        @Volatile
        private var INSTANCE: BillDb? = null

        fun getInstance(context: Context): BillDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BillDb::class.java,
                        "bill.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}