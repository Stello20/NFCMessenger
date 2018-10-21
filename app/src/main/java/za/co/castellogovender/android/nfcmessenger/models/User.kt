package za.co.castellogovender.android.nfcmessenger.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid:String, val username:String):Parcelable{
    constructor(): this("", "")
}