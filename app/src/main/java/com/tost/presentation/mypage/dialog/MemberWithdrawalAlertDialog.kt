package com.tost.presentation.mypage.dialog

import android.content.Context
import com.tost.R

/**
 * Created By Malibin
 * on 3월 16, 2021
 */

class MemberWithdrawalAlertDialog(context: Context) : SimpleAlertDialog(context) {
    override val contentText: String = context.getString(R.string.member_withdrawal_wait)
    override val buttonText: String = context.getString(R.string.yes_i_am_fine)
}
