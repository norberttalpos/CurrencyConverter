package hu.bme.aut.currencyconverter.data.repository.selection

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CurrencySelectionDao {
    @Query("SELECT * FROM currencySelection")
    fun getAll(): List<CurrencySelection>

    @Query("SELECT * FROM currencySelection WHERE selected = 1 AND base = 0")
    fun getSelected(): List<CurrencySelection>

    @Query("SELECT * FROM currencySelection WHERE base = 1")
    fun getBase(): CurrencySelection?

    @Insert
    fun insert(currency: CurrencySelection): Long

    @Query("DELETE FROM currencySelection WHERE name = :currencyName")
    fun delete(currencyName: String)

    @Query("DELETE FROM currencySelection")
    fun deleteAll(): Unit

    @Transaction
    fun changeBase(previous: CurrencySelection, current: CurrencySelection) {
        delete(current.name)
        delete(previous.name)
        insert(CurrencySelection(name = current.name, base = true))
        insert(CurrencySelection(name = previous.name, base = false))
    }

    @Transaction
    fun toggleSelection(selectionToToggle: CurrencySelection) {
        delete(selectionToToggle.name)

        val name = selectionToToggle.name

        val selected = !selectionToToggle.selected!!

        var base: Boolean? = false
        if(selectionToToggle.selected == true)
            base = selectionToToggle.base
        else
            base = false

        insert(CurrencySelection(name = name, selected = selected, base = base))
    }
}