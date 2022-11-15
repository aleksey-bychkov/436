package com.example.myapplication

class Message(
    private var userEmail: String = "",
    private var targetUID: String = "",
    private var msg: String = "",
    private var dateTime: String = ""
) {



    fun setUserEmail(userEmail: String): Unit{
        this.userEmail = userEmail
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

    fun getUserEmail(): String {
        return this.userEmail
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
}