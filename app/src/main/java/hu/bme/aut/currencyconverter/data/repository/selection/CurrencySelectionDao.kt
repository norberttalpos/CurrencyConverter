package hu.bme.aut.currencyconverter.data.repository.selection

import androidx.room.*

@Dao
interface CurrencySelectionDao {
    @Query("SELECT * FROM currencySelection")
    fun getAll(): List<CurrencySelection>

    @Query("SELECT * FROM currencySelection WHERE selected = 1 AND base = 0")
    fun getSelected(): List<CurrencySelection>

    @Query("SELECT * FROM currencySelection WHERE base = 1")
    fun getBase(): CurrencySelection?

    @Query("SELECT * FROM currencySelection WHERE name LIKE '%' || :searchText || '%'")
    fun search(searchText: String): List<CurrencySelection>

    @Insert
    fun insert(currency: CurrencySelection): Long

    @Update
    fun update(currency: CurrencySelection)

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

        val baseAfterToggle: Boolean = if(selectionToToggle.selected)
            selectionToToggle.base
        else
            false

        val needsNewBase = !selectionToToggle.selected && selectionToToggle.base

        selectionToToggle.base = baseAfterToggle
        update(selectionToToggle)

        if(needsNewBase) {
            val newBaseCurrency = this.getAll().first {
                it.selected
            }

            newBaseCurrency.base = true

            update(newBaseCurrency)
        }
    }
}