package com.example.myapplication

class Contacts(
    private var username: String = "",
    private var targetUID: String = "",
    private var msg: String = "",
    private var msgID: String = "",
    private var dateTime: String = "",
    private var isReported: Boolean = false,
    private var isRead: Boolean = false,
    private var isBlocked: Boolean = false
) {




    fun setUsername(username: String): Unit{
        this.username = username
    }

    fun setMsg(msg: String): Unit{
        this.msg = msg
    }

    fun setMsgID(msgID: String): Unit{
        this.msgID = msgID
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

    fun setIsRead(status: Boolean): Unit{
        this.isRead = status
    }

    fun setIsBlocked(status: Boolean): Unit{
        this.isBlocked = status
    }

    fun getUsername(): String {
        return this.username
    }

    fun getMsg(): String {
        return this.msg
    }

    fun getMsgID(): String {
        return this.msgID
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

    fun getIsRead(): Boolean {
        return this.isRead
    }

    fun getIsBlocked(): Boolean {
        return this.isBlocked
    }


}