package hu.bme.aut.currencyconverter.data.repository.conversion

import androidx.room.*
import com.google.gson.annotations.SerializedName
import hu.bme.aut.currencyconverter.data.CurrencyEnum
import hu.bme.aut.currencyconverter.data.repository.selection.CurrencySelection

@Entity(tableName = "conversion")
data class Conversion(
    @ColumnInfo(name = "conversion_id")
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "from")
    var from: CurrencyEnum,

    @ColumnInfo(name = "to")
    var to: CurrencyEnum,

    @ColumnInfo(name = "amount")
    var amount: Int,

    @ColumnInfo(name = "result")
    var result: Double,
)
