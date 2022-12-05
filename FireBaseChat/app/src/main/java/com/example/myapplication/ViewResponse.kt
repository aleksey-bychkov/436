package com.example.myapplication

class ViewResponse(
    private var uid: String = "",
    private var answer1: Int = 0,
    private var answer2: Int = 0,
    private var answer3: Int = 0,
    private var answer4: Int = 0,
    private var answer5: Int = 0,
    private var agg: Float = 0F) {

    // class used to represent an instance of a survey response in the database
    // contains information about the answers, aggregate score, and submitter ID

    fun setUid(uid: String): Unit{
        this.uid = uid
    }

    fun setAnswer1(response: Int): Unit{
        this.answer1 = response
    }

    fun setAnswer2(response: Int): Unit{
        this.answer2 = response
    }

    fun setAnswer3(response: Int): Unit{
        this.answer3 = response
    }

    fun setAnswer4(response: Int): Unit{
        this.answer4 = response
    }

    fun setAnswer5(response: Int): Unit{
        this.answer5 = response
    }

    fun setAgg(agg: Float): Unit{
        this.agg = agg
    }

    fun getUID(): String {
        return this.uid
    }

    fun getAnswer1(): Int {
        return this.answer1
    }

    fun getAnswer2(): Int {
        return this.answer2
    }

    fun getAnswer3(): Int {
        return this.answer3
    }

    fun getAnswer4(): Int {
        return this.answer4
    }

    fun getAnswer5(): Int {
        return this.answer5
    }

    fun getAgg(): Float {
        return this.agg
    }


}