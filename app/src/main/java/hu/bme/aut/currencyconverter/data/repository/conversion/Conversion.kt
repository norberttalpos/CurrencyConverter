package hu.bme.aut.currencyconverter.data.repository.conversion

import androidx.room.Entity
import java.util.*

@Entity(tableName = "conversion")
data class Conversion(
    val from: Currency,
    val to: Currency,
    val amount: Double,
    val date: Date,
)
