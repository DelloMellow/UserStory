package com.example.userstory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var token: String? = null,
    var isLoggedIn: Boolean = false
): Parcelable
