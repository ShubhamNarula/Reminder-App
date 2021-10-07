package com.techskuad.reminderapp.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.edit_reminder.*
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    //get the current time
    fun getTimeCalendar(): Pair<Int, Int> {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        return Pair(first = hour, second = minute)

    }

    //get the current date
    fun getDateCalendar() : Time {
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        return Time(day,month,year)
    }
    data class Time(val day: Int, val month: Int, val year: Int)


    fun getCurrentDateWithFormat() : String{
        val currentDate = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault()).format(Date())
        return currentDate
    }

    fun getCurrentTimeWithFormat() : String{
        val currentTime = SimpleDateFormat(Constants.TIME_FORMAT, Locale.getDefault()).format(Date())
        return currentTime
    }

    fun currentDateAndTime(timeFormat: String): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat(timeFormat)
        val formattedDate = df.format(c.time)
        return formattedDate
    }

    fun getFutureDate(timeFormat: String,days:Int):String{
        val c = Calendar.getInstance()
        val df = SimpleDateFormat(timeFormat)
        c.add(Calendar.DAY_OF_MONTH, days)
        val formattedDate = df.format(c.time)
        return formattedDate
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun timeConverter(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        calendar.set(year, month, day,hour, minute)
        val format = SimpleDateFormat(Constants.TIME_FORMAT)
        val strDate: String = format.format(calendar.time)
        return strDate
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun dateConverter(year: Int,month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val format = SimpleDateFormat(Constants.DATE_FORMAT)
        val strDate: String = format.format(calendar.time)
        return strDate
    }


    /*compare date and time*/
    fun compareDateAndTime(
        currenDateAndTime: String,
        selectedDateAndTime: String,
        dateFormat: String?
    ): Boolean {
        var isDateCompare = false
        val sdformat = SimpleDateFormat(dateFormat)
        val d1 = sdformat.parse(currenDateAndTime)
        val d2 = sdformat.parse(selectedDateAndTime)
        println("The date 1 is: " + sdformat.format(d1))
        println("The date 2 is: " + sdformat.format(d2))
        if (d1.compareTo(d2) > 0) {
            // When Date d1 > Date d2
            isDateCompare = false

        } else if (d1.compareTo(d2) < 0) {
            // When Date d1 < Date d2
            isDateCompare = true

            println("Date 1 occurs before Date 2")
        } else if (d1.compareTo(d2) == 0) {
            // When Date d1 = Date d2
            isDateCompare = false

            println("Both dates are equal")
        }
        return isDateCompare
    }

    fun getDayMonthYearFromString(strDate:String):Time{
        val dateParts: List<String> = strDate.split("/")
        val day = dateParts[0].toInt()
        val month = dateParts[1].toInt()-1
        val year = dateParts[2].toInt()
        return Time(day,month,year)
    }

}