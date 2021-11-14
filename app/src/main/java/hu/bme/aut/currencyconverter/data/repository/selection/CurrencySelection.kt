package hu.bme.aut.currencyconverter.data.repository.selection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencySelection")
data class CurrencySelection(

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "selected")
    var selected: Boolean? = true,

    @ColumnInfo(name = "base")
    var base: Boolean? = false,
)
