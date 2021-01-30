package com.adityaverma.notes.utils

import android.text.format.DateFormat
import java.util.*

/**
 * Created by Aditya Verma on 30-01-2021.
 */
open class ConvertToDate {

    fun convertTimeStampToDate(timeStamp: Long): String {
        val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timeStamp
        return DateFormat.format("dd MMM yyyy", cal).toString()
    }

}