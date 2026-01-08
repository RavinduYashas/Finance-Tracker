package com.example.financetrackerex3

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val date: String
)  : Serializable





//
//import android.os.Parcel
//import android.os.Parcelable
//
//data class Transaction(
//    val id: Int,
//    val title: String,
//    val amount: Double,
//    val category: String,
//    val date: String
//) : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readInt(),
//        parcel.readString() ?: "",
//        parcel.readDouble(),
//        parcel.readString() ?: "",
//        parcel.readString() ?: ""
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeInt(id)
//        parcel.writeString(title)
//        parcel.writeDouble(amount)
//        parcel.writeString(category)
//        parcel.writeString(date)
//    }
//
//    override fun describeContents(): Int = 0
//
//    companion object {
//        @JvmField
//        val CREATOR = object : Parcelable.Creator<Transaction> {
//            override fun createFromParcel(parcel: Parcel): Transaction {
//                return Transaction(parcel)
//            }
//
//            override fun newArray(size: Int): Array<Transaction?> {
//                return arrayOfNulls(size)
//            }
//        }
//    }
//}
// Transaction.kt (can reuse, but update to Room format)
