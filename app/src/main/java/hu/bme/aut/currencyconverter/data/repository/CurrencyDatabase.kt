package hu.bme.aut.currencyconverter.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.bme.aut.currencyconverter.data.repository.conversion.Conversion
import hu.bme.aut.currencyconverter.data.repository.conversion.ConversionDao
import hu.bme.aut.currencyconverter.data.repository.selection.CurrencySelection
import hu.bme.aut.currencyconverter.data.repository.selection.CurrencySelectionDao

@Database(entities = [CurrencySelection::class, Conversion::class], version = 1)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencySelectionDao(): CurrencySelectionDao
    abstract fun conversionListDao(): ConversionDao

    companion object {
        fun getDatabase(applicationContext: Context): CurrencyDatabase {
            return Room.databaseBuilder(
                applicationContext,
                CurrencyDatabase::class.java,
                "shopping-list"
            ).build();
        }
    }
}
