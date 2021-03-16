package com.tost.presentation.mypage.dialog

import android.content.Context
import com.tost.R

/**
 * Created By Malibin
 * on 3월 16, 2021
 */

class LogoutAlertDialog(context: Context) : SimpleAlertDialog(context) {
    override val contentText: String = context.getString(R.string.logout_wait)
    override val buttonText: String = context.getString(R.string.logout_at_moment)
}
