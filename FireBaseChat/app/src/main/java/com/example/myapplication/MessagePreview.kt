package com.example.myapplication

class MessagePreview(
    private var username: String = "",
    private var targetUID: String = "",
    private var msg: String = "",
    private var dateTime: String = "",
    private var isReported: Boolean = false,

) {




    fun setUsername(username: String): Unit{
        this.username = username
    }

    fun setMsg(msg: String): Unit{
        this.msg = msg
    }

    fun setDateTime(dateTime: String): Unit{
        this.dateTime = dateTime
    }

    fun setTargetUID(targetUID: String): Unit{
        this.targetUID = targetUID
    }

    fun setIsReported(status: Boolean): Unit{
        this.isReported = status
    }

    fun getUsername(): String {
        return this.username
    }

    fun getMsg(): String {
        return this.msg
    }

    fun getDateTime(): String {
        return this.dateTime
    }

    fun getTargetUID(): String {
        return this.targetUID
    }

    fun getIsReported(): Boolean {
        return this.isReported
    }


}