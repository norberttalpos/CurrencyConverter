package hu.bme.aut.currencyconverter.data.repository.conversion

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ConversionDao {
    @Query("SELECT * FROM conversion")
    fun getAll(): List<Conversion>

    @Insert
    fun insert(conversion: Conversion): Long

    @Delete
    fun delete(conversion: Conversion)
}