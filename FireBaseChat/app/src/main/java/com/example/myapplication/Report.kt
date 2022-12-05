package com.example.myapplication

class Report(
    private var messageID: String = "",
    private var msg: String = "",
    private var extraInfo: String = "",
    private var reportedUID: String = "",
    private var reportingUID: String = "",
    private var dateTime: String = "",
    private var resolved: Boolean = false
) {
    // class used to represent an instance of a report in the database
    // contains information about the message, submitter ID, reason for report,
    // reported user ID, and whether the report has been resolved

    fun setMessageID(messageID: String): Unit{
        this.messageID = messageID
    }

    fun setReportedUID(reportedUID: String): Unit{
        this.reportedUID = reportedUID
    }

    fun setMsg(msg: String): Unit{
        this.msg = msg
    }

    fun setExtraInfo(info: String): Unit{
        this.extraInfo = info
    }

    fun setDateTime(dateTime: String): Unit{
        this.dateTime = dateTime
    }

    fun setReportingUID(reportingUID: String): Unit{
        this.reportingUID = reportingUID
    }

    fun setResolved(status: Boolean): Unit{
        this.resolved = status
    }

    fun getReportingUID(): String {
        return this.reportingUID
    }

    fun getMsg(): String {
        return this.msg
    }

    fun getExtraInfo(): String {
        return this.extraInfo
    }

    fun getDateTime(): String {
        return this.dateTime
    }

    fun getReportedUID(): String {
        return this.reportedUID
    }

    fun getResolved(): Boolean {
        return this.resolved
    }

    fun getMessageID(): String{
        return this.messageID
    }

}