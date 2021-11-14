package hu.bme.aut.currencyconverter.data.repository.selection

import androidx.room.*

@Dao
interface CurrencySelectionDao {
    @Query("SELECT * FROM currencySelection")
    fun getAll(): List<CurrencyName>

    @Insert
    fun insert(currencyName: CurrencyName): Long

    @Query("DELETE FROM currencySelection WHERE name = :currencyName")
    fun delete(currencyName: String)

    @Query("DELETE FROM currencySelection")
    fun deleteAll(): Unit

    @Transaction
    fun changeBase(previous: CurrencyName, current: CurrencyName) {
        insert(previous)
        delete(current.name)
    }
}