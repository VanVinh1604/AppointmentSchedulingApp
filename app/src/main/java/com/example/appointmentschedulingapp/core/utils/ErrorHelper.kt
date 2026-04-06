package com.example.appointmentschedulingapp.core.helper

object ErrorHelper {

    fun toFriendlyMessage(e: Throwable): String {
        val message = e.message.orEmpty()

        return when {
            message.contains("invalid", ignoreCase = true) ->
                "Số điện thoại không hợp lệ"

            message.contains("blocked", ignoreCase = true) ->
                "Số điện thoại bị chặn, vui lòng thử số khác"

            message.contains("network", ignoreCase = true) ->
                "Lỗi mạng, vui lòng kiểm tra kết nối"

            message.contains("too-many-requests", ignoreCase = true) ->
                "Quá nhiều yêu cầu, vui lòng thử lại sau"

            message.contains("bad code", ignoreCase = true) ->
                "Mã OTP không đúng, vui lòng thử lại"

            message.contains("expired", ignoreCase = true) ->
                "Mã OTP đã hết hạn, vui lòng gửi lại"

            else -> "Đã có lỗi xảy ra, vui lòng thử lại"
        }
    }
}