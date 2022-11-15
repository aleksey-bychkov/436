package com.example.myapplication

class Message(
    private var userID: String = "",
    private var targetUID: String = "",
    private var msg: String = "",
    private var dateTime: String = "",
    private var isReported: Boolean = false,
    private var messageID: String = ""
) {


    fun setMessageID(messageID: String): Unit{
        this.messageID = messageID
    }

    fun setUserID(userID: String): Unit{
        this.userID = userID
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

    fun getUserID(): String {
        return this.userID
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

    fun getMessageID(): String{
        return this.messageID
    }

}