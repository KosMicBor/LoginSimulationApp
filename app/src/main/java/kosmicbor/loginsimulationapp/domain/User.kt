package kosmicbor.loginsimulationapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userNickname: String,
    val userLogin: String,
    var userPassword: String,
) : Parcelable