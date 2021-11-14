package hu.bme.aut.currencyconverter.data.repository.selection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencySelection")
data class CurrencyName(

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "name")
    var name: String,
)
