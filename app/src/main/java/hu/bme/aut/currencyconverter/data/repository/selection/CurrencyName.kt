package hu.bme.aut.currencyconverter.data.repository.selection

import androidx.room.Entity

@Entity(tableName = "currencySelection")
data class CurrencyName(
    var name: String,
)
