package com.techskuad.reminderapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_table")
data class ReminderModel (
    @ColumnInfo(name = "title")
    var title:String?,
    @ColumnInfo(name = "description")
    val description :String?,
    @ColumnInfo(name = "time")
    val time:String?,
    @ColumnInfo(name = "date")
    val date:String?,
    @ColumnInfo(name = "type")
    val type:String?,
    @PrimaryKey(autoGenerate = true)
    val id:Int=0
    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(time)
        parcel.writeString(date)
        parcel.writeString(type)
        if (id != null) {
            parcel.writeInt(id)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReminderModel> {
        override fun createFromParcel(parcel: Parcel): ReminderModel {
            return ReminderModel(parcel)
        }

        override fun newArray(size: Int): Array<ReminderModel?> {
            return arrayOfNulls(size)
        }
    }
}
