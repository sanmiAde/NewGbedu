package com.sanmiaderibigbe.newgbedu.utils

import java.text.SimpleDateFormat
import java.util.*



fun convertDateStringToDateObject(dateString: String): Date {
    val format = SimpleDateFormat("d MMMM yyyy")
    return format.parse(dateString)
}

 fun convertDateToString(date: Date): String {
    val dateFormat = SimpleDateFormat("d MMMM yyyy")
    return dateFormat.format(date)
}