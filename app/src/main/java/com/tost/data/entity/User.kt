package com.tost.data.entity

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

/**
 * Created By Malibin
 * on 12월 20, 2020
 */

data class User(
    val email: String,
    val name: String,
    val profileUrl: String,
    val googleToken: String,
    val tostToken: String,
) {
    companion object {
        fun from(account: GoogleSignInAccount, tostToken: String): User = User(
            email = account.email.orEmpty(),
            name = account.displayName.orEmpty(),
            profileUrl = account.photoUrl.toString(),
            googleToken = account.idToken.orEmpty(),
            tostToken = tostToken
        )
    }
}
