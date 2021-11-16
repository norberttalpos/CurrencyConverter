package hu.bme.aut.currencyconverter.data

import androidx.room.TypeConverter

enum class CurrencyEnum {
    HUF, USD, EUR, CHF, GBP,
    JPY, AUD, CAD, CNY, HKD,
    NZD, SEK, KRW, SGD, NOK,
    MXN, INR, RUB, ZAR, TRY,
    BRL, TWD, DKK, PLN, THB,
    IDR, CZK, ILS, CLP, PHP,
    AED, COP, SAR, MYR, RON
    ;

    companion object {
        @JvmStatic
        @TypeConverter
        fun getByOrdinal(ordinal: Int): CurrencyEnum? {
            var ret: CurrencyEnum? = null
            for (currency in values()) {
                if (currency.ordinal == ordinal) {
                    ret = currency
                    break
                }
            }
            return ret
        }

        @JvmStatic
        @TypeConverter
        fun toInt(currency: CurrencyEnum): Int {
            return currency.ordinal
        }
    }
}