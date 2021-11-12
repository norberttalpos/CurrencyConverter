package hu.bme.aut.currencyconverter.data.repository.selection

import androidx.room.*

@Dao
interface CurrencySelectionDao {
    @Query("SELECT * FROM currencySelection")
    fun getAll(): List<CurrencyName>

    @Insert
    fun insert(currencyName: CurrencyName): Long

    @Delete
    fun delete(currencyName: CurrencyName)
}